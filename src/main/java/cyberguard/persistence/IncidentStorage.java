package cyberguard.persistence;

import cyberguard.domain.*;
import cyberguard.service.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;

public class IncidentStorage {
    private Path file;

    public IncidentStorage(Path file) {
        this.file = file;
    }

    public void save(ArrayList<Incident> incidents) throws IOException {
        Path absolute = this.file.toAbsolutePath();
        Files.createDirectories(absolute.getParent());
        Path temporary = Files.createTempFile(absolute.getParent(), "incidents-", ".tmp");
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(temporary, StandardCharsets.UTF_8)) {
                writer.write("CYBERGUARD-1");
                writer.newLine();
                for (Incident incident : incidents) {
                    writer.write(incident.getId() + "\t" + incident.getType() + "\t" +
                            incident.getTitle() + "\t" + incident.getDescription() + "\t" +
                            incident.getAsset().getName() + "\t" + incident.getSeverity() + "\t" +
                            incident.getStatus());
                    writer.newLine();
                }
            }
            // Write the complete replacement before touching the previous saved file.
            Files.move(temporary, absolute, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    public ArrayList<Incident> load() throws IOException {
        IncidentManager loaded = new IncidentManager();
        if (!Files.exists(this.file)) return loaded.getIncidents();
        try (BufferedReader reader = Files.newBufferedReader(this.file, StandardCharsets.UTF_8)) {
            if (!"CYBERGUARD-1".equals(reader.readLine())) {
                throw new IOException("Unknown or missing file header.");
            }
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    String[] fields = line.split("\t", -1);
                    if (fields.length != 7) throw new IncidentException("Expected seven fields.");
                    IncidentFactory factory;
                    if (fields[1].equals("PHISHING")) factory = new PhishingFactory();
                    else if (fields[1].equals("MALWARE")) factory = new MalwareFactory();
                    else throw new IncidentException("Unknown incident type.");
                    Incident incident = factory.createIncident(Integer.parseInt(fields[0]), fields[2],
                            fields[3], new Asset(fields[4]), Severity.valueOf(fields[5]));
                    Status status = Status.valueOf(fields[6]);
                    if (status != Status.OPEN) incident.changeStatus(Status.INVESTIGATING);
                    if (status == Status.RESOLVED) incident.changeStatus(Status.RESOLVED);
                    loaded.addIncident(incident);
                } catch (IllegalArgumentException | IncidentException e) {
                    throw new IOException("Invalid record on line " + lineNumber + ": " + e.getMessage(), e);
                }
            }
        }
        return loaded.getIncidents();
    }
}
