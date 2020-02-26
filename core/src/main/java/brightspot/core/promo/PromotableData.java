package brightspot.core.promo;

import brightspot.core.image.ImageOption;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

/**
 * The {@link PromotableData} class contains the {@link Modification} data of the {@link Promotable} interface.
 */
@Recordable.FieldInternalNamePrefix("promotable.")
public class PromotableData extends Modification<Promotable> {

    private static final String PROMOTABLE_TAB = "Overrides";

    @ToolUi.Tab(PROMOTABLE_TAB)
    @ToolUi.Cluster("Dynamic Results")
    @Indexed
    private boolean hideFromDynamicResults;

    /**
     * Helper method to assist with redirects of deprecated getters & setters
     *
     * @return Original object as PromotableWithOverridesData instance.
     */
    private PromotableWithOverridesData asPromotableWithOverridesData() {
        return getOriginalObject().as(PromotableWithOverridesData.class);
    }

    /**
     * Return the {@code promo title} if it's set, otherwise return the promotable's fallback
     *
     * @return a plain text {@link String} (optional).
     */
    @Deprecated
    public String getPromoTitle() {
        if (getOriginalObject().isInstantiableTo(PromotableWithOverrides.class)) {
            return asPromotableWithOverridesData().getPromoTitle();
        }
        return null;
    }

    /**
     * Sets the {@code promo title}.
     *
     * @param promoTitle a plain text {@link String} (optional).
     */
    @Deprecated
    public void setPromoTitle(String promoTitle) {
        if (getOriginalObject().isInstantiableTo(PromotableWithOverrides.class)) {
            asPromotableWithOverridesData().setPromoTitle(promoTitle);
        }
    }

    /**
     * Return the {@code promo description} if it's set, otherwise return the promotable's fallback
     *
     * @return a plain text {@link String} (optional).
     */
    @Deprecated
    public String getPromoDescription() {
        if (getOriginalObject().isInstantiableTo(PromotableWithOverrides.class)) {
            return asPromotableWithOverridesData().getPromoDescription();
        }
        return null;
    }

    /**
     * Sets the {@code promo description}.
     *
     * @param promoDescription a plain text {@link String} (optional).
     */
    @Deprecated
    public void setPromoDescription(String promoDescription) {
        if (getOriginalObject().isInstantiableTo(PromotableWithOverrides.class)) {
            asPromotableWithOverridesData().setPromoDescription(promoDescription);
        }
    }

    /**
     * Return the {@code promo image} if it's set, otherwise return the promotable's fallback
     *
     * @return an {@link ImageOption} (optional).
     */
    @Deprecated
    public ImageOption getPromoImage() {
        if (getOriginalObject().isInstantiableTo(PromotableWithOverrides.class)) {
            return asPromotableWithOverridesData().getPromoImage();
        }
        return null;
    }

    /**
     * Sets the {@code promo image}.
     *
     * @param promoImage an {@link ImageOption} (optional).
     */
    @Deprecated
    public void setPromoImage(ImageOption promoImage) {
        if (getOriginalObject().isInstantiableTo(PromotableWithOverrides.class)) {
            asPromotableWithOverridesData().setPromoImage(promoImage);
        }
    }

    /**
     * Return the {@code hideFromDynamicResults}
     *
     * @return an {@link boolean} (optional).
     */
    public boolean isHiddenFromDynamicResults() {
        return hideFromDynamicResults;
    }

    /**
     * Sets the {@code hideFromDynamicResults}.
     *
     * @param hideFromDynamicResults an {@link boolean} (optional).
     */
    public void setHideFromDynamicResults(boolean hideFromDynamicResults) {
        this.hideFromDynamicResults = hideFromDynamicResults;
    }

    /**
     * Returns the preview HTML for the {@code promoImage}, using the image returned by {@link
     * PromotableWithOverrides#getPromotableImageFallback()}.
     * <p/>
     * <strong>Note:</strong> For internal use only!
     *
     * @return a HTML {@link String} (optional).
     */
    @Deprecated
    public String getPromotableImagePreviewHtml() {
        if (getOriginalObject().isInstantiableTo(PromotableWithOverrides.class)) {
            return asPromotableWithOverridesData().getPromotableImagePreviewHtml();
        }
        return null;
    }

    /**
     * Returns the placeholder HTML for the {@code promoImage}, using the image returned by {@link
     * PromotableWithOverrides#getPromotableImageFallback()}.
     * <p/>
     * <strong>Note:</strong> For internal use only!
     *
     * @return a HTML {@link String} (optional).
     */
    @Deprecated
    public String getPromotableImagePlaceholderHtml() {

        if (getOriginalObject().isInstantiableTo(PromotableWithOverrides.class)) {
            return asPromotableWithOverridesData().getPromotableImagePlaceholderHtml();
        }
        return null;
    }
}
