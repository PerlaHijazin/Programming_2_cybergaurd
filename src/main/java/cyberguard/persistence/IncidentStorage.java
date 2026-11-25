package cyberguard.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import cyberguard.domain.*;
import cyberguard.service.IncidentFactory;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Saves incidents to and loads them from a JSON file.
public class IncidentStorage {
    private final Path file;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public IncidentStorage(Path file) {
        this.file = file;
    }

    // Plain data shape of one incident inside the JSON file.
    private record IncidentRecord(int id, String type, String title, String description,
                                  String asset, Severity severity, Status status) { }

    public void save(List<Incident> incidents) throws IOException {
        List<IncidentRecord> records = new ArrayList<>();
        for (Incident i : incidents) {
            records.add(new IncidentRecord(i.getId(), i.getType(), i.getTitle(), i.getDescription(),
                    i.getAsset().getName(), i.getSeverity(), i.getStatus()));
        }
        Path parent = file.toAbsolutePath().getParent();
        Files.createDirectories(parent);
        try (Writer writer = Files.newBufferedWriter(file)) {
            gson.toJson(records, writer);
        }
    }

    public List<Incident> load() throws IOException {
        List<Incident> incidents = new ArrayList<>();
        if (!Files.exists(file)) return incidents;
        try (Reader reader = Files.newBufferedReader(file)) {
            List<IncidentRecord> records = gson.fromJson(reader, new TypeToken<List<IncidentRecord>>() { }.getType());
            if (records == null) return incidents;
            for (IncidentRecord r : records) {
                if (r == null) throw new IncidentException("Empty record.");
                incidents.add(IncidentFactory.forType(r.type()).createIncident(
                        r.id(), r.title(), r.description(), new Asset(r.asset()), r.severity(), r.status()));
            }
        } catch (JsonParseException | IncidentException e) {
            throw new IOException("The data file is invalid: " + e.getMessage(), e);
        }
        return incidents;
    }
}
