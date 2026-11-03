package cyberguard.service;

import cyberguard.domain.Asset;
import cyberguard.domain.Incident;
import cyberguard.domain.PhishingIncident;
import cyberguard.domain.Severity;

public class PhishingFactory extends IncidentFactory {
    @Override
    public Incident createIncident(int id, String title, String description,
                                   Asset asset, Severity severity) {
        return new PhishingIncident(id, title, description, asset, severity);
    }
}
