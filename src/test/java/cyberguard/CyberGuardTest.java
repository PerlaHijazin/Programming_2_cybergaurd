package cyberguard;

import cyberguard.domain.*;
import cyberguard.persistence.IncidentStorage;
import cyberguard.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CyberGuardTest {
    @TempDir
    Path folder;

    private Incident phishing(int id, Severity severity) {
        return new PhishingFactory().createIncident(id, "Fake login", "Suspicious email",
                new Asset("Office PC"), severity, Status.OPEN);
    }

    @Test
    void factoriesCreateDifferentBehaviorsThroughTheSameParent() {
        Incident first = IncidentFactory.forType("PHISHING")
                .createIncident(1, "Email", "Fake login", new Asset("PC"), Severity.HIGH, Status.OPEN);
        Incident second = IncidentFactory.forType("MALWARE")
                .createIncident(2, "Virus", "Unknown program", new Asset("PC"), Severity.HIGH, Status.OPEN);
        assertInstanceOf(PhishingIncident.class, first);
        assertInstanceOf(MalwareIncident.class, second);
        assertNotEquals(first.getResponseAdvice(), second.getResponseAdvice());
        assertThrows(IncidentException.class, () -> IncidentFactory.forType("RANSOMWARE"));
    }

    @Test
    void invalidObjectsAreRejected() {
        assertThrows(IncidentException.class, () -> phishing(0, Severity.LOW));
        assertThrows(IncidentException.class, () -> phishing(1, null));
        assertThrows(IncidentException.class, () -> new Asset(" "));
    }

    @Test
    void statusMustFollowTheInvestigationWorkflow() {
        Incident incident = phishing(1, Severity.HIGH);
        assertThrows(IncidentException.class, () -> incident.changeStatus(Status.RESOLVED));
        incident.changeStatus(Status.INVESTIGATING);
        incident.changeStatus(Status.RESOLVED);
        assertEquals(Status.RESOLVED, incident.getStatus());
        assertThrows(IncidentException.class, () -> incident.changeStatus(Status.OPEN));
    }

    @Test
    void sortingFilteringAndIdsWorkTogether() {
        IncidentManager manager = new IncidentManager();
        manager.addIncident(phishing(4, Severity.LOW));
        manager.addIncident(phishing(2, Severity.CRITICAL));
        manager.addIncident(phishing(1, Severity.CRITICAL));
        manager.findIncident(2).changeStatus(Status.INVESTIGATING);

        List<Incident> sorted = manager.getSortedIncidents();
        assertEquals(1, sorted.get(0).getId());
        assertEquals(2, sorted.get(1).getId());
        assertEquals(4, sorted.get(2).getId());
        assertEquals(5, manager.getNextId());
        assertEquals(2, manager.findByStatus(Status.OPEN).size());
        assertThrows(IncidentException.class, () -> manager.addIncident(phishing(4, Severity.LOW)));
        assertThrows(IncidentException.class, () -> manager.findIncident(99));
    }

    @Test
    void incidentsAreSavedToJsonAndLoadedBack() throws IOException {
        Incident first = new PhishingFactory().createIncident(1, "Fake login", "Suspicious email",
                new Asset("Office PC"), Severity.CRITICAL, Status.RESOLVED);
        Incident second = new MalwareFactory().createIncident(2, "Virus", "Unexpected file",
                new Asset("Laptop"), Severity.MEDIUM, Status.OPEN);
        IncidentStorage storage = new IncidentStorage(folder.resolve("nested/incidents.json"));
        storage.save(List.of(first, second));

        String json = Files.readString(folder.resolve("nested/incidents.json"));
        assertTrue(json.contains("\"type\": \"PHISHING\""));

        List<Incident> loaded = storage.load();
        assertEquals(2, loaded.size());
        assertInstanceOf(PhishingIncident.class, loaded.get(0));
        assertInstanceOf(MalwareIncident.class, loaded.get(1));
        assertEquals(Status.RESOLVED, loaded.get(0).getStatus());
        assertEquals(Severity.CRITICAL, loaded.get(0).getSeverity());
        assertEquals("Office PC", loaded.get(0).getAsset().getName());
    }

    @Test
    void missingFileStartsEmptyAndInvalidFileFailsClearly() throws IOException {
        Path file = folder.resolve("incidents.json");
        IncidentStorage storage = new IncidentStorage(file);
        assertTrue(storage.load().isEmpty());

        Files.writeString(file, "this is not json");
        assertThrows(IOException.class, storage::load);

        Files.writeString(file, "[{\"id\": 1, \"type\": \"UNKNOWN\", \"title\": \"T\", \"description\": \"D\","
                + " \"asset\": \"PC\", \"severity\": \"LOW\", \"status\": \"OPEN\"}]");
        assertThrows(IOException.class, storage::load);
    }
}
