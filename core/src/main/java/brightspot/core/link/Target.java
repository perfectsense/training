package brightspot.core.link;

public enum Target {

    NEW("New Window/Tab", "_blank");

    private final String label;
    private final String value;

    Target(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return label;
    }
}
