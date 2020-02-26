package brightspot.core.promo;

import java.util.Optional;

import brightspot.core.image.ImageOption;
import brightspot.core.tool.ImagePreviewHtml;
import com.psddev.cms.db.ImageTag;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

/**
 * The {@link PromotableWithOverridesData} class contains the {@link Modification} data of the {@link
 * PromotableWithOverrides} interface.
 */
@Recordable.FieldInternalNamePrefix("promotable.")
public class PromotableWithOverridesData extends Modification<PromotableWithOverrides> implements ImagePreviewHtml {

    private static final String PROMOTABLE_TAB = "Overrides";
    protected static final String PROMO_MODULE_OVERRIDES_CLUSTER_NAME = "Promo Module Overrides (Internal)";

    @ToolUi.Cluster(PROMO_MODULE_OVERRIDES_CLUSTER_NAME)
    @ToolUi.Tab(PROMOTABLE_TAB)
    @ToolUi.Placeholder(dynamicText = "${content.getPromotableTitleFallback()}", editable = true)
    private String promoTitle;

    @ToolUi.Cluster(PROMO_MODULE_OVERRIDES_CLUSTER_NAME)
    @ToolUi.Tab(PROMOTABLE_TAB)
    @ToolUi.Placeholder(dynamicText = "${content.getPromotableDescriptionFallback()}", editable = true)
    private String promoDescription;

    @ToolUi.Cluster(PROMO_MODULE_OVERRIDES_CLUSTER_NAME)
    @ToolUi.Tab(PROMOTABLE_TAB)
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getPromotableImagePlaceholderHtml()}'></span>")
    private ImageOption promoImage;

    /**
     * Return the {@code promo title} if it's set, otherwise return the promotable's fallback
     *
     * @return a plain text {@link String} (optional).
     */
    @ToolUi.Hidden
    @Ignored(false)
    public String getPromoTitle() {
        return ObjectUtils.firstNonBlank(promoTitle, getOriginalObject().getPromotableTitleFallback());
    }

    /**
     * Sets the {@code promo title}.
     *
     * @param promoTitle a plain text {@link String} (optional).
     */
    public void setPromoTitle(String promoTitle) {
        this.promoTitle = promoTitle;
    }

    /**
     * Return the {@code promo description} if it's set, otherwise return the promotable's fallback
     *
     * @return a plain text {@link String} (optional).
     */
    @ToolUi.Hidden
    @Ignored(false)
    public String getPromoDescription() {

        return ObjectUtils.firstNonBlank(promoDescription, getOriginalObject().getPromotableDescriptionFallback());
    }

    /**
     * Sets the {@code promo description}.
     *
     * @param promoDescription a plain text {@link String} (optional).
     */
    public void setPromoDescription(String promoDescription) {
        this.promoDescription = promoDescription;
    }

    /**
     * Return the {@code promo image} if it's set, otherwise return the promotable's fallback
     *
     * @return an {@link ImageOption} (optional).
     */
    public ImageOption getPromoImage() {

        return ObjectUtils.firstNonNull(promoImage, getOriginalObject().getPromotableImageFallback());
    }

    /**
     * Sets the {@code promo image}.
     *
     * @param promoImage an {@link ImageOption} (optional).
     */
    public void setPromoImage(ImageOption promoImage) {
        this.promoImage = promoImage;
    }

    /**
     * Returns the preview HTML for the {@code promoImage}, using the image returned by {@link
     * PromotableWithOverrides#getPromotableImageFallback()}.
     * <p/>
     * <strong>Note:</strong> For internal use only!
     *
     * @return a HTML {@link String} (optional).
     */
    public String getPromotableImagePreviewHtml() {

        if (promoImage != null) {
            return null;
        }

        return Optional.ofNullable(getOriginalObject().getPromotableImageFallback())
            .map(ImageOption::getImageOptionFile)
            .map(file -> new ImageTag.Builder(file).setHeight(100).hideDimensions().toHtml())
            .orElse(null);
    }

    /**
     * Returns the placeholder HTML for the {@code promoImage}, using the image returned by {@link
     * PromotableWithOverrides#getPromotableImageFallback()}.
     * <p/>
     * <strong>Note:</strong> For internal use only!
     *
     * @return a HTML {@link String} (optional).
     */
    public String getPromotableImagePlaceholderHtml() {

        if (promoImage != null) {
            return null;
        }

        return writePreviewImageHtml(getOriginalObject().getPromotableImageFallback());
    }
}
