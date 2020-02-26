package brightspot.core.search;

public class FilterItem {

    private final String label;
    private final boolean selected;
    private final String url;
    private final long count;
    private final String value;

    public FilterItem(String label, boolean selected, String url, long count) {
        this.label = label;
        this.selected = selected;
        this.url = url;
        this.count = count;
        this.value = null;
    }

    public FilterItem(String label, boolean selected, String url, long count, String value) {
        this.label = label;
        this.selected = selected;
        this.url = url;
        this.count = count;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getUrl() {
        return url;
    }

    public long getCount() {
        return count;
    }

    public String getValue() {
        return value;
    }
}
