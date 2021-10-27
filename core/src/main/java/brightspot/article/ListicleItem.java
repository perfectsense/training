package brightspot.article;

public class ListicleItem {

    private String title;

    private boolean firstEntry;

    private String body;

    private Listicle listicle;

    public ListicleItem() {
    }

    public ListicleItem(String title, boolean firstEntry, String body, Listicle listicle) {
        this.title = title;
        this.firstEntry = firstEntry;
        this.body = body;
        this.listicle = listicle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isFirstEntry() {
        return firstEntry;
    }

    public void setFirstEntry(boolean firstEntry) {
        this.firstEntry = firstEntry;
    }

    public Listicle getListicle() {
        return listicle;
    }

    public void setListicle(Listicle listicle) {
        this.listicle = listicle;
    }
}
