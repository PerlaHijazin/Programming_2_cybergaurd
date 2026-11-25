package cyberguard.service;

import cyberguard.domain.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IncidentManager {
    private final List<Incident> incidents = new ArrayList<>();

    public int getNextId() {
        int max = 0;
        for (Incident incident : incidents) max = Math.max(max, incident.getId());
        return max + 1;
    }

    public void addIncident(Incident incident) {
        if (incident == null) throw new IncidentException("Incident is required.");
        for (Incident existing : incidents) {
            if (existing.getId() == incident.getId()) {
                throw new IncidentException("An incident with ID " + incident.getId() + " already exists.");
            }
        }
        incidents.add(incident);
    }

    public Incident findIncident(int id) {
        for (Incident incident : incidents) {
            if (incident.getId() == id) return incident;
        }
        throw new IncidentException("Incident not found: " + id);
    }

    // Returns a copy so callers cannot change the manager's list directly.
    public List<Incident> getIncidents() {
        return new ArrayList<>(incidents);
    }

    public List<Incident> getSortedIncidents() {
        List<Incident> sorted = getIncidents();
        Collections.sort(sorted);
        return sorted;
    }

    public List<Incident> findByStatus(Status status) {
        List<Incident> found = getSortedIncidents();
        found.removeIf(incident -> incident.getStatus() != status);
        return found;
    }
}
