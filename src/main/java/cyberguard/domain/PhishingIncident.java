package cyberguard.domain;

public class PhishingIncident extends Incident {
    public PhishingIncident(int id, String title, String description, Asset asset, Severity severity, Status status) {
        super(id, title, description, asset, severity, status);
    }

    @Override
    public String getType() {
        return "PHISHING";
    }

    @Override
    public String getResponseAdvice() {
        return "Block the sender, report the message, and reset exposed credentials.";
    }
}
