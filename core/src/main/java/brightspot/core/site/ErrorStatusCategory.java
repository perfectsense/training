package brightspot.core.site;

public enum ErrorStatusCategory {

    CLIENT_ERROR("4xx Client Error", 400, 499),
    SERVER_ERROR("5xx Server Error", 500, 599);

    private final String label;
    private final int minimum;
    private final int maximum;

    ErrorStatusCategory(String label, int minimum, int maximum) {
        this.label = label;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public int getMinimum() {
        return minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public boolean matches(int statusCode) {
        return statusCode >= minimum && statusCode <= maximum;
    }

    @Override
    public String toString() {
        return label;
    }
}
