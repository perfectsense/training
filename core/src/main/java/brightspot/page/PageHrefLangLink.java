package brightspot.page;

public class PageHrefLangLink {

    private String languageCode;

    private String url;

    public PageHrefLangLink(String languageCode, String url) {
        this.languageCode = languageCode;
        this.url = url;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
