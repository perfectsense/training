package brightspot.core.promo;

import brightspot.core.image.ImageOption;
import brightspot.core.link.InternalLink;
import brightspot.core.link.Target;
import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;

public interface PromoItem extends Recordable {

    Promotable getPromoItemPromotable();

    String getPromoItemUrl(Site site);

    String getPromoItemTitle();

    String getPromoItemDescription();

    ImageOption getPromoItemImage();

    static PromoItem createDefault() {
        return DefaultImplementationSupplier.createDefault(PromoItem.class, InternalLink.class);
    }

    /**
     * Create an object to be passed to the view system.
     *
     * @param overrides is never null.
     */
    Object createPromoWrapper(Promo overrides);

    default Target getTarget() {
        return null;
    }
}
