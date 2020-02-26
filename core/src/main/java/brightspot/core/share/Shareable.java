package brightspot.core.share;

import brightspot.core.image.ImageOption;
import brightspot.core.tool.DirectoryItemUtils;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;

/**
 * The {@link Shareable} interface provides a marker interface for content that can be shared externally, as well as
 * provide fallback values for internal use.
 * <p/>
 * The {@link ShareableData} contains the actual methods used for external consumptions (with the exception of {@link
 * Shareable#getShareableUrl(Site)}).
 */
public interface Shareable extends Recordable {

    /**
     * Convenience method for returning the {@link ShareableData} of this object.
     *
     * @return the {@link ShareableData} (optional).
     */
    default ShareableData asShareableData() {
        return as(ShareableData.class);
    }

    /**
     * Returns a fallback value for the {@code title} or "headline" used when sharing content externally.
     * <p/>
     * <strong>Note:</strong> for internal use only!
     *
     * @return a plain text {@link String} (optional).
     */
    default String getShareableTitleFallback() {
        return null;
    }

    /**
     * Returns a fallback value for the {@code description} or "subHeadline" used when sharing content externally.
     * <p/>
     * <strong>Note:</strong> for internal use only!
     *
     * @return a plain text {@link String} (optional).
     */
    default String getShareableDescriptionFallback() {
        return null;
    }

    /**
     * Returns a fallback value for the {@code image} used when sharing content externally.
     * <p/>
     * <strong>Note:</strong> for internal use only!
     *
     * @return an {@link ImageOption} (optional).
     */
    default ImageOption getShareableImageFallback() {
        return null;
    }

    /**
     * Returns the {@code url} used when sharing content externally.
     *
     * @param site the {@link Site} to use as the basis of the URL (nullable).
     * @return an plain text {@link String} containing the canonical URL of the content based on the provided {@link
     * Site} (optional).
     */
    default String getShareableUrl(Site site) {
        return DirectoryItemUtils.getCanonicalUrl(site, this);
    }
}
