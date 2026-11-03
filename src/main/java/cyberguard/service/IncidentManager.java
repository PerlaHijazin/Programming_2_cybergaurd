package cyberguard.service;

import cyberguard.domain.*;
import java.util.ArrayList;
import java.util.Collections;

public class IncidentManager {
    private ArrayList<Incident> incidents;

    public IncidentManager() {
        this.incidents = new ArrayList<>();
    }

    public int getNextId() {
        int max = 0;
        for (Incident incident : this.incidents) {
            if (incident.getId() > max) max = incident.getId();
        }
        if (max == Integer.MAX_VALUE) throw new IncidentException("No more IDs available.");
        return max + 1;
    }

    public void addIncident(Incident incident) {
        if (incident == null) throw new IncidentException("Incident is required.");
        for (Incident existing : this.incidents) {
            if (existing.getId() == incident.getId()) {
                throw new IncidentException("An incident with that ID already exists.");
            }
        }
        this.incidents.add(incident);
    }

    public Incident findIncident(int id) {
        for (Incident incident : this.incidents) {
            if (incident.getId() == id) return incident;
        }
        throw new IncidentException("Incident not found: " + id);
    }

    public ArrayList<Incident> getIncidents() {
        return new ArrayList<>(this.incidents);
    }

    public ArrayList<Incident> getSortedIncidents() {
        ArrayList<Incident> sorted = getIncidents();
        Collections.sort(sorted);
        return sorted;
    }

    public ArrayList<Incident> findByStatus(Status status) {
        ArrayList<Incident> found = new ArrayList<>();
        for (Incident incident : getSortedIncidents()) {
            if (incident.getStatus() == status) found.add(incident);
        }
        return found;
    }
}
