package cyberguard.domain;

public class Asset {
    private String name;

    public Asset(String name) {
        this.name = Incident.validateText(name, "Asset name");
    }

    public String getName() {
        return this.name;
    }
}
