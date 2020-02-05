package brightspot.core.video;

public enum Preload {
    NONE("None", "none"),
    AUTO("Auto", "auto"),
    METADATA("Metadata", "metadata");

    private String label;

    private String htmlValue;

    Preload(String label, String htmlValue) {
        this.label = label;
        this.htmlValue = htmlValue;
    }

    public String toString() {
        return label;
    }

    public String getHtmlValue() {
        return htmlValue;
    }
}
