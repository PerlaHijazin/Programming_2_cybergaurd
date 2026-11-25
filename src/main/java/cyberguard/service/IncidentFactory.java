package cyberguard.service;

import cyberguard.domain.*;

// Factory Method: each subclass decides which concrete Incident to create.
public abstract class IncidentFactory {
    public abstract Incident createIncident(int id, String title, String description,
                                            Asset asset, Severity severity, Status status);

    // One place that maps a type name to its factory (used by the menu and by storage).
    public static IncidentFactory forType(String type) {
        if ("PHISHING".equals(type)) return new PhishingFactory();
        if ("MALWARE".equals(type)) return new MalwareFactory();
        throw new IncidentException("Unknown incident type: " + type);
    }
}
