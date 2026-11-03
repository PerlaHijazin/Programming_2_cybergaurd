package cyberguard.service;

import cyberguard.domain.Asset;
import cyberguard.domain.Incident;
import cyberguard.domain.Severity;

public abstract class IncidentFactory {
    public abstract Incident createIncident(int id, String title, String description,
                                            Asset asset, Severity severity);
}
