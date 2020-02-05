package brightspot.core.link;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.anchor.Anchorable;
import brightspot.core.anchor.Anchorage;
import brightspot.core.image.ImageOption;
import brightspot.core.promo.Promo;
import brightspot.core.promo.PromoItem;
import brightspot.core.promo.Promotable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.DisplayName("Internal")
public class InternalLink extends Link implements PromoItem {

    @Required
    @ToolUi.DisplayFirst
    @ToolUi.OnlyPathed
    @ToolUi.NestedRepeatable
    private Linkable item;

    @ToolUi.Tab(Link.ADVANCED_TAB)
    @ToolUi.DisplayBefore("target")
    @ToolUi.NoteHtml("<span data-dynamic-html='${content.createAnchorHtml()}'></span>")
    private String anchor;

    public Linkable getItem() {
        return item;
    }

    public void setItem(Linkable item) {
        this.item = item;
    }

    public String getAnchor() {
        return anchor;
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    @Override
    public String getLinkUrl(Site site) {
        Linkable item = getItem();
        String anchorString = (ObjectUtils.isBlank(getAnchor()) ? "" : "#" + getAnchor());
        return item != null ? item.getLinkableUrl(site) + anchorString : null;
    }

    @Override
    public String getLinkTextFallback() {
        Linkable item = getItem();
        return item != null ? item.getLinkableText() : super.getLinkTextFallback();
    }

    public String createAnchorHtml() {
        return Optional.ofNullable(getItem())
            .filter(linkable -> linkable.isInstantiableTo(Anchorage.class))
            .map(linkable -> linkable.as(Anchorage.class))
            .map(Anchorage::getAnchors)
            .orElseGet(Collections::emptySet)
            .stream()
            .map(Anchorable::getAnchorableAnchor)
            .collect(Collectors.joining(" | "));
    }

    @Override
    public Promotable getPromoItemPromotable() {
        return Optional.ofNullable(getItem())
            .map(item -> item.as(Promotable.class))
            .orElse(null);
    }

    @Override
    public String getPromoItemUrl(Site site) {
        return Optional.ofNullable(getItem())
            .map(item -> item.getLinkableUrl(site))
            .orElse(null);
    }

    @Override
    public String getPromoItemTitle() {
        return Optional.ofNullable(getPromoItemPromotable())
            .map(Promotable::getPromotableTitle)
            .orElse(null);
    }

    @Override
    public String getPromoItemDescription() {
        return Optional.ofNullable(getPromoItemPromotable())
            .map(Promotable::getPromotableDescription)
            .orElse(null);
    }

    @Override
    public ImageOption getPromoItemImage() {
        return Optional.ofNullable(getPromoItemPromotable())
            .map(Promotable::getPromotableImage)
            .orElse(null);
    }

    @Override
    public Object createPromoWrapper(Promo overrides) {
        return Optional.ofNullable(getPromoItemPromotable())
            .map(promotable -> promotable.createPromoWrapper(overrides))
            .orElse(null);
    }

}
