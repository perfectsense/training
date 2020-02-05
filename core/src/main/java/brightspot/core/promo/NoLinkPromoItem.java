package brightspot.core.promo;

import java.util.Optional;

import brightspot.core.image.ImageOption;
import brightspot.core.link.InternalLink;
import brightspot.core.link.Linkable;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("No Link")
@Recordable.Embedded
public class NoLinkPromoItem extends Record implements Interchangeable, PromoItem {

    @ToolUi.Unlabeled
    @ToolUi.DisplayFirst
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
        return null;
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
    public String getLabel() {
        return getPromoItemTitle();
    }

    @Override
    public Object createPromoWrapper(Promo overrides) {
        if (overrides == null) {
            throw new IllegalArgumentException("Overrides cannot be null!");
        }

        return new NoLinkPromoWrapper(this, overrides);
    }

    @Override
    public boolean loadTo(Object newObject) {
        Promotable originalItem = this.getItem();
        if (newObject instanceof InternalPromoItem) {
            ((InternalPromoItem) newObject).setItem(originalItem);
            return true;
        }
        if (newObject instanceof InternalLink
            && originalItem != null
            && originalItem.isInstantiableTo(Linkable.class)) {
            ((InternalLink) newObject).setItem(originalItem.as(Linkable.class));
            return true;
        }
        return false;
    }

    public boolean loadFrom(Object original) {
        if (original instanceof InternalPromoItem) {
            this.setItem(((InternalPromoItem) original).getItem());
            return true;
        }
        if (original instanceof InternalLink) {
            Linkable originalItem = ((InternalLink) original).getItem();
            if (originalItem != null && originalItem.isInstantiableTo(Promotable.class)) {
                this.setItem(originalItem.as(Promotable.class));
                return true;
            }
        }
        return false;
    }
}
