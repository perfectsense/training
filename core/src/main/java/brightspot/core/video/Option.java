package brightspot.core.video;

public enum Option {

    MUTED("Muted"),
    AUTOPLAY("Autoplay"),
    CARD("Card Overlay"),
    PLAYLIST("Playlist");

    private String label;

    Option(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
