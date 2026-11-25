package cyberguard.domain;

public abstract class Incident implements Comparable<Incident> {
    private final int id;
    private final String title;
    private final String description;
    private final Asset asset;
    private Severity severity;
    private Status status;

    protected Incident(int id, String title, String description, Asset asset, Severity severity, Status status) {
        if (id <= 0) throw new IncidentException("ID must be positive.");
        if (asset == null) throw new IncidentException("An affected asset is required.");
        if (status == null) throw new IncidentException("Status is required.");
        this.id = id;
        this.title = requireText(title, "Title");
        this.description = requireText(description, "Description");
        this.asset = asset;
        this.status = status;
        setSeverity(severity);
    }

    static String requireText(String text, String field) {
        if (text == null || text.isBlank()) throw new IncidentException(field + " cannot be empty.");
        return text.trim();
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Asset getAsset() { return asset; }
    public Severity getSeverity() { return severity; }
    public Status getStatus() { return status; }

    public final void setSeverity(Severity severity) {
        if (severity == null) throw new IncidentException("Severity is required.");
        this.severity = severity;
    }

    public void changeStatus(Status next) {
        if (next == null || !status.canMoveTo(next)) {
            throw new IncidentException("Follow the order OPEN -> INVESTIGATING -> RESOLVED.");
        }
        status = next;
    }

    // Each subclass gives its own answer (runtime polymorphism).
    public abstract String getType();
    public abstract String getResponseAdvice();

    // Highest severity first; equal severity sorted by ID.
    @Override
    public int compareTo(Incident other) {
        int result = other.severity.compareTo(severity);
        return result != 0 ? result : Integer.compare(id, other.id);
    }

    @Override
    public String toString() {
        return "#" + id + " " + title
                + "\n  Type: " + getType() + " | Severity: " + severity + " | Status: " + status
                + "\n  Description: " + description
                + "\n  Asset: " + asset.getName()
                + "\n  Advice: " + getResponseAdvice();
    }
}
