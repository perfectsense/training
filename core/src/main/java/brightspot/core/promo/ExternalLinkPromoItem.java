package brightspot.core.promo;

import brightspot.core.image.ImageOption;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * @deprecated Replaced by {@link brightspot.core.link.ExternalLink}.
 */
@Deprecated
@Recordable.Embedded
@Recordable.DisplayName("External (Deprecated)")
public class ExternalLinkPromoItem extends Record implements PromoItem {

    @ToolUi.Unlabeled
    @Required
    @ToolUi.DisplayFirst
    @ToolUi.Placeholder("http://...")
    private String url;

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
    public PromoWrapper createPromoWrapper(Promo overrides) {
        if (overrides == null) {
            throw new IllegalArgumentException("Overrides cannot be null!");
        }

        return new ExternalPromoWrapper(this, overrides);
    }
}
