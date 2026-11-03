package cyberguard.domain;

public abstract class Incident implements Comparable<Incident> {
    private int id;
    private String title;
    private String description;
    private Asset asset;
    private Severity severity;
    private Status status;

    public Incident(int id, String title, String description, Asset asset, Severity severity) {
        if (id <= 0) throw new IncidentException("ID must be positive.");
        if (asset == null) throw new IncidentException("An affected asset is required.");
        this.id = id;
        this.title = validateText(title, "Title");
        this.description = validateText(description, "Description");
        this.asset = asset;
        setSeverity(severity);
        this.status = Status.OPEN;
    }

    public static String validateText(String text, String field) {
        if (text == null || text.trim().isEmpty()) {
            throw new IncidentException(field + " cannot be empty.");
        }
        if (text.contains("\t") || text.contains("\n") || text.contains("\r")) {
            throw new IncidentException(field + " must be one line without tabs.");
        }
        return text.trim();
    }

    public int getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public Asset getAsset() { return this.asset; }
    public Severity getSeverity() { return this.severity; }
    public Status getStatus() { return this.status; }

    public void setSeverity(Severity severity) {
        if (severity == null) throw new IncidentException("Severity is required.");
        this.severity = severity;
    }

    public void changeStatus(Status newStatus) {
        if (newStatus == null) throw new IncidentException("Status is required.");
        if (newStatus == this.status) return;
        if (this.status == Status.OPEN && newStatus == Status.INVESTIGATING ||
                this.status == Status.INVESTIGATING && newStatus == Status.RESOLVED) {
            this.status = newStatus;
        } else {
            throw new IncidentException("Follow the order OPEN -> INVESTIGATING -> RESOLVED.");
        }
    }

    public abstract String getType();
    public abstract String getResponseAdvice();

    // Higher severity comes first. Equal severity is sorted by ID.
    @Override
    public int compareTo(Incident other) {
        int result = other.severity.compareTo(this.severity);
        if (result == 0) return Integer.compare(this.id, other.id);
        return result;
    }
}
