package cyberguard.service;

import cyberguard.domain.*;

public class PhishingFactory extends IncidentFactory {
    @Override
    public Incident createIncident(int id, String title, String description,
                                   Asset asset, Severity severity, Status status) {
        return new PhishingIncident(id, title, description, asset, severity, status);
    }
}
