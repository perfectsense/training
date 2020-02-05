package brightspot.core.promo;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import brightspot.core.footer.FooterModuleType;
import brightspot.core.image.ImageOption;
import brightspot.core.lead.Lead;
import brightspot.core.link.ExternalLink;
import brightspot.core.link.InternalLink;
import brightspot.core.link.Linkable;
import brightspot.core.link.Target;
import brightspot.core.module.ModuleType;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.ImageTag;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;

public class Promo extends ModuleType implements Lead, ModelWrapper, FooterModuleType, PromoOption {

    @Required
    private PromoItem item = PromoItem.createDefault();

    @Deprecated
    @ToolUi.Tab("Overrides")
    @ToolUi.Cluster("Link")
    private Target linkTarget;

    @ToolUi.Placeholder(dynamicText = "${content.getTitlePlaceholder()}", editable = true)
    private String title;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Placeholder(dynamicText = "${content.getDescriptionPlaceholder()}", editable = true)
    private String description;

    @ToolUi.NoteHtml("<span data-dynamic-html='${content.getImagePlaceholderHtml()}'></span>")
    private ImageOption image;

    public Optional<Promotable> getContent() {
        return Optional.ofNullable(getItem())
            .map(PromoItem::getPromoItemPromotable);
    }

    public Target getLinkTarget() {
        return Optional.ofNullable(item)
            .map(PromoItem::getTarget)
            .orElse(linkTarget);
    }

    public void setLinkTarget(Target linkTarget) {
        this.linkTarget = linkTarget;
    }

    /**
     * Return the override title if it is set, otherwise the title placeholder
     */
    public String getTitle() {
        return !ObjectUtils.isBlank(title)
            ? title
            : getTitlePlaceholder();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Return the description if it is set, otherwise the description placeholder
     *
     * @return an inline RichText {@link String} (optional).
     */
    public String getDescription() {
        return !ObjectUtils.isBlank(description)
            ? description
            : getDescriptionPlaceholder();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageOption getImageOption() {
        return image;
    }

    public void setImageOption(ImageOption image) {
        this.image = image;
    }

    public Optional<PromoItem> getPromoItem() {
        return Optional.ofNullable(getItem());
    }

    /**
     * Return the override image if set, otherwise the image placeholder
     */
    public ImageOption getImage() {
        return Optional.ofNullable(getImageOption())
            .orElse(getImagePlaceholder());
    }

    /**
     * Return the underlying content of the selected item _if_ it is an InternalPromoItem that is Promotable.
     *
     * @return an {@link Optional} containing a {@link Promotable} (never null).
     */
    public Optional<Promotable> getPromotable() {
        return Optional.ofNullable(getItem())
            .map(PromoItem::getPromoItemPromotable);
    }

    /**
     * Return the URL of the selected item
     */
    public String getUrl(Site site) {
        return Optional.ofNullable(getItem())
            .map(promoItem -> promoItem.getPromoItemUrl(site))
            .orElse(null);
    }

    /**
     * Return the placeholder title - override this in implementations that are for non-Promotable items
     */
    public String getTitlePlaceholder() {
        return getPromoItem()
            .map(PromoItem::getPromoItemTitle)
            .orElse(null);
    }

    /**
     * Return the placeholder description - override this in implementations that are for non-Promotable items
     */
    public String getDescriptionPlaceholder() {
        return getPromoItem()
            .map(PromoItem::getPromoItemDescription)
            .orElse(null);
    }

    /**
     * Return the placeholder image - override this in implementations that are for non-Promotable items
     */
    public ImageOption getImagePlaceholder() {
        return getPromoItem()
            .map(PromoItem::getPromoItemImage)
            .orElse(null);
    }

    public String getImagePreviewHtml() {

        if (image != null) {
            return null;
        }

        return Optional.ofNullable(getImagePlaceholder())
            .map(ImageOption::getImageOptionFile)
            .map(file -> new ImageTag.Builder(file).setHeight(100).hideDimensions().toHtml())
            .orElse(null);
    }

    public String getImagePlaceholderHtml() {

        String previewHtml = getImagePreviewHtml();
        if (ObjectUtils.isBlank(previewHtml)) {
            return "";
        }
        return previewHtml + "<br>" + StringUtils.escapeHtml("Default Promo Image");
    }

    @Relocate
    public PromoItem getItem() {
        // Relocate legacy promo item types
        if (item instanceof InternalPromoItem && item.getPromoItemPromotable() != null) {
            Promotable promotable = item.getPromoItemPromotable();

            if (promotable.isInstantiableTo(Linkable.class)) {
                InternalLink internalLink = new InternalLink();
                internalLink.getState().setId(item.getState().getId());
                internalLink.setItem(promotable.as(Linkable.class));
                item = internalLink;
            }

        } else if (item instanceof ExternalLinkPromoItem && item.getPromoItemUrl(null) != null) {
            ExternalLink externalLink = new ExternalLink();
            externalLink.getState().setId(item.getState().getId());
            externalLink.setUrl(item.getPromoItemUrl(null));
            item = externalLink;
        }

        return item;
    }

    public void setItem(PromoItem item) {
        this.item = item;
    }

    @Override
    public ImageOption getLeadImage() {
        return getImage();
    }

    @Override
    public String getLabel() {
        return Optional.ofNullable(item)
            .map(PromoItem::getPromoItemTitle)
            .orElse(null);
    }

    @Override
    public Object unwrap() {
        return getPromoItem()
            .map(promoItem -> promoItem.createPromoWrapper(this))
            .orElseGet(() -> new PromoWrapper(null, this));
    }

    @Override
    public Promo getPromoOptionPromo() {
        return this;
    }

    @Override
    public Set<UUID> getModuleTypeContentIds() {
        return getContent()
            .map(Promotable::getState)
            .map(State::getId)
            .map(Collections::singleton)
            .orElse(Collections.emptySet());
    }
}
