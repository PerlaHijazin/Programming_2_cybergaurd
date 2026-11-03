package cyberguard;

import cyberguard.domain.*;
import cyberguard.service.*;
import cyberguard.persistence.IncidentStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class CyberGuardTest {
    @TempDir
    Path folder;

    private Incident phishing(int id, Severity severity) {
        IncidentFactory factory = new PhishingFactory();
        return factory.createIncident(id, "Fake login", "Suspicious email", new Asset("Office PC"), severity);
    }

    @Test
    void factoriesCreateDifferentBehaviorsThroughTheSameParent() {
        IncidentFactory factory = new PhishingFactory();
        Incident first = factory.createIncident(1, "Email", "Fake login", new Asset("PC"), Severity.HIGH);
        factory = new MalwareFactory();
        Incident second = factory.createIncident(2, "Virus", "Unknown program", new Asset("PC"), Severity.HIGH);
        assertEquals("PHISHING", first.getType());
        assertEquals("MALWARE", second.getType());
        assertNotEquals(first.getResponseAdvice(), second.getResponseAdvice());
    }

    @Test
    void invalidObjectsAreRejected() {
        assertThrows(IncidentException.class, () -> phishing(0, Severity.LOW));
        assertThrows(IncidentException.class, () -> phishing(1, null));
        assertThrows(IncidentException.class, () -> new Asset(" "));
        assertThrows(IncidentException.class, () -> new Asset("PC\tServer"));
    }

    @Test
    void statusMustFollowTheInvestigationWorkflow() {
        Incident incident = phishing(1, Severity.HIGH);
        assertThrows(IncidentException.class, () -> incident.changeStatus(Status.RESOLVED));
        assertEquals(Status.OPEN, incident.getStatus());
        incident.changeStatus(Status.INVESTIGATING);
        incident.changeStatus(Status.RESOLVED);
        assertThrows(IncidentException.class, () -> incident.changeStatus(Status.OPEN));
    }

    @Test
    void sortingFilteringAndIdsWorkTogether() {
        IncidentManager manager = new IncidentManager();
        manager.addIncident(phishing(4, Severity.LOW));
        manager.addIncident(phishing(2, Severity.CRITICAL));
        manager.addIncident(phishing(1, Severity.CRITICAL));
        manager.findIncident(2).changeStatus(Status.INVESTIGATING);
        assertEquals(1, manager.getSortedIncidents().get(0).getId());
        assertEquals(2, manager.getSortedIncidents().get(1).getId());
        assertEquals(5, manager.getNextId());
        assertEquals(2, manager.findByStatus(Status.OPEN).size());
        assertThrows(IncidentException.class, () -> manager.addIncident(phishing(4, Severity.LOW)));
        assertThrows(IncidentException.class, () -> manager.findIncident(99));
        manager.getIncidents().clear();
        assertEquals(3, manager.getIncidents().size());
    }

    @Test
    void recordsRoundTripIncludingBothTypesAndResolvedStatus() throws IOException {
        IncidentManager manager = new IncidentManager();
        Incident first = phishing(1, Severity.CRITICAL);
        first.changeStatus(Status.INVESTIGATING);
        first.changeStatus(Status.RESOLVED);
        manager.addIncident(first);
        manager.addIncident(new MalwareFactory().createIncident(2, "Virus", "Unexpected file",
                new Asset("Laptop"), Severity.MEDIUM));
        IncidentStorage storage = new IncidentStorage(folder.resolve("nested/incidents.txt"));
        storage.save(manager.getIncidents());
        ArrayList<Incident> loaded = storage.load();
        assertEquals(2, loaded.size());
        assertEquals(Status.RESOLVED, loaded.get(0).getStatus());
        assertEquals(Severity.CRITICAL, loaded.get(0).getSeverity());
        assertEquals("Fake login", loaded.get(0).getTitle());
        assertEquals("Suspicious email", loaded.get(0).getDescription());
        assertEquals("Office PC", loaded.get(0).getAsset().getName());
        assertInstanceOf(PhishingIncident.class, loaded.get(0));
        assertInstanceOf(MalwareIncident.class, loaded.get(1));
    }

    @Test
    void missingFileStartsEmptyAndCorruptRecordsFailClearly() throws IOException {
        Path file = folder.resolve("incidents.txt");
        IncidentStorage storage = new IncidentStorage(file);
        assertTrue(storage.load().isEmpty());
        Files.writeString(file, "CYBERGUARD-1\ninvalid\n");
        IOException error = assertThrows(IOException.class, storage::load);
        assertTrue(error.getMessage().contains("line 2"));
        Files.writeString(file, "CYBERGUARD-1\n1\tPHISHING\tTitle\tDescription\tPC\tHIGH\tOPEN\n" +
                "1\tMALWARE\tTitle\tDescription\tPC\tLOW\tOPEN\n");
        assertThrows(IOException.class, storage::load);
    }
}
