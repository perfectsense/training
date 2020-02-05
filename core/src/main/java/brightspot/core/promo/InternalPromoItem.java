package brightspot.core.promo;

import java.util.Optional;

import brightspot.core.image.ImageOption;
import brightspot.core.link.Linkable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * @deprecated Replaced by {@link brightspot.core.link.InternalLink}.
 */
@Deprecated
@Recordable.Embedded
@Recordable.DisplayName("Internal (Deprecated)")
public class InternalPromoItem extends Record implements PromoItem {

    @ToolUi.Unlabeled
    @Required
    @ToolUi.DisplayFirst
    @ToolUi.NestedRepeatable
    private Promotable item;

    public Promotable getItem() {
        return item;
    }

    public void setItem(Promotable item) {
        this.item = item;
    }

    @Override
    public Promotable getPromoItemPromotable() {
        return getItem();
    }

    @Override
    public String getPromoItemUrl(Site site) {
        return getLinkableItem()
            .map(linkable -> linkable.getLinkableUrl(site))
            .orElse(null);
    }

    public Optional<Linkable> getLinkableItem() {
        return Optional.ofNullable(getItem())
            .filter(Linkable.class::isInstance)
            .map(Linkable.class::cast);
    }

    public Optional<Promotable> getPromotable() {
        return Optional.ofNullable(getItem());
    }

    @Override
    public String getPromoItemTitle() {
        return getPromotable()
            .map(Promotable::getPromotableTitle)
            .orElse(null);
    }

    @Override
    public String getPromoItemDescription() {
        return getPromotable()
            .map(Promotable::getPromotableDescription)
            .orElse(null);
    }

    @Override
    public ImageOption getPromoItemImage() {
        return getPromotable()
            .map(Promotable::getPromotableImage)
            .orElse(null);
    }

    @Override
    public Object createPromoWrapper(Promo overrides) {
        return getPromotable()
            .map(promotable -> promotable.createPromoWrapper(overrides))
            .orElse(null);
    }
}
