package brightspot.core.quote;

import brightspot.core.footer.FooterModuleType;
import brightspot.core.image.ImageOption;
import brightspot.core.module.ModuleType;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Pull Quote")
public class PullQuoteModule extends ModuleType implements FooterModuleType {

    @Required
    @Indexed
    private String quote;

    @Indexed
    private String attribution;

    private ImageOption attributionImage;

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public ImageOption getAttributionImage() {
        return attributionImage;
    }

    public void setAttributionImage(ImageOption attributionImage) {
        this.attributionImage = attributionImage;
    }
}
