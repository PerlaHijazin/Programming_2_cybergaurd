package cyberguard.domain;

public class Asset {
    private final String name;

    public Asset(String name) {
        this.name = Incident.requireText(name, "Asset name");
    }

    public String getName() {
        return name;
    }
}
