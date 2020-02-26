package brightspot.core.link;

import brightspot.core.image.ImageOption;
import brightspot.core.promo.ExternalLinkPromoWrapper;
import brightspot.core.promo.Promo;
import brightspot.core.promo.PromoItem;
import brightspot.core.promo.Promotable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("External")
public class ExternalLink extends Link implements PromoItem {

    public ExternalLink() {
        super();
        this.setTarget(Target.NEW);
    }

    @Required
    @ToolUi.Note("Start all external links with http://")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getLinkUrl(Site site) {
        return getUrl();
    }

    @Override
    public Promotable getPromoItemPromotable() {
        return null;
    }

    @Override
    public String getPromoItemUrl(Site site) {
        return url;
    }

    @Override
    public String getPromoItemTitle() {
        return null;
    }

    @Override
    public String getPromoItemDescription() {
        return null;
    }

    @Override
    public ImageOption getPromoItemImage() {
        return null;
    }

    @Override
    public Object createPromoWrapper(Promo overrides) {
        if (overrides == null) {
            throw new IllegalArgumentException("Overrides cannot be null!");
        }

        return new ExternalLinkPromoWrapper(this, overrides);
    }
}
