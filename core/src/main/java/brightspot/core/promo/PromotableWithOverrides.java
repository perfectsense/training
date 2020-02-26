package brightspot.core.promo;

import brightspot.core.image.ImageOption;

/**
 * The {@link PromotableWithOverrides} interface provides the ability to override {@link Promotable} values through
 * values within the {@link PromotableData} object modification, as well as provide fallback values for internal use.
 */
public interface PromotableWithOverrides extends Promotable {

    /**
     * Convenience method for returning the {@link PromotableData} of this object.
     *
     * @return the {@link PromotableData} (optional).
     */
    default PromotableData asPromotableData() {
        return as(PromotableData.class);
    }

    /**
     * Returns a fallback value for the {@code title} or "headline" used when promoting content internally. This is the
     * value used by default, unless an editor provides a title in the override tab.
     * <p/>
     * <strong>Note:</strong> for internal use only!
     *
     * @return a plain text {@link String} (optional).
     */
    default String getPromotableTitleFallback() {
        return null;
    }

    /**
     * Returns a fallback value for the {@code description} or "subHeadline" used when sharing content externally. This
     * is the value used by default, unless an editor provides a description in the override tab.
     * <p/>
     * <strong>Note:</strong> for internal use only!
     *
     * @return a plain text {@link String} (optional).
     */
    default String getPromotableDescriptionFallback() {
        return null;
    }

    /**
     * Returns a fallback value for the {@code image} used when promoting content internally. This is the value used by
     * default, unless an editor provides an image in the override tab.
     * <p/>
     * <strong>Note:</strong> for internal use only!
     *
     * @return an {@link ImageOption} (optional).
     */
    default ImageOption getPromotableImageFallback() {
        return null;
    }

    @Override
    default String getPromotableTitle() {
        return asPromotableData().getPromoTitle();
    }

    @Override
    default String getPromotableDescription() {
        return asPromotableData().getPromoDescription();
    }

    @Override
    default ImageOption getPromotableImage() {
        return asPromotableData().getPromoImage();
    }
}
