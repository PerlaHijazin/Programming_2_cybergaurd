package cyberguard.domain;

public enum Status {
    OPEN, INVESTIGATING, RESOLVED;

    // An incident may stay where it is or move exactly one step forward.
    public boolean canMoveTo(Status next) {
        return next == this || next.ordinal() == this.ordinal() + 1;
    }
}
