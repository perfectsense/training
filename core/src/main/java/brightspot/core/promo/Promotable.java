package brightspot.core.promo;

import java.util.Date;
import java.util.Optional;

import brightspot.core.image.ImageOption;
import brightspot.core.section.Section;
import brightspot.core.section.Sectionable;
import brightspot.core.tool.DirectoryItemUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;

/**
 * The {@link Promotable} interface provides the contract for content that can be promoted (shared) internally.
 */
public interface Promotable extends Recordable {

    String INTERNAL_NAME = "brightspot.core.promo.Promotable";
    String DATE_FORMAT_KEY = "dateFormat";

    /**
     * Returns the {@code title} or "headline" used when promoting content internally.
     *
     * @return a plain text {@link String} (optional).
     */
    String getPromotableTitle();

    /**
     * Returns the {@code description} or "subHeadline" used when promoting content internally.
     *
     * @return a plain text {@link String} (optional).
     */
    String getPromotableDescription();

    /**
     * Returns the {@code image} used when promoting content internally.
     *
     * @return an {@link ImageOption} (optional).
     */
    ImageOption getPromotableImage();

    /**
     * Returns the {@code type} of the content being promoted.
     *
     * @return a plain text {@link String} (optional).
     */
    default String getPromotableType() {
        return null;
    }

    /**
     * Returns the {@code category} of the content being promoted.
     * <p/>
     * Defaults to the {@link Section} of the content, if {@link Sectionable}.
     *
     * @return a plain text {@link String} (optional).
     */
    default String getPromotableCategory() {
        return Optional.of(this)
            .filter(Sectionable.class::isInstance)
            .map(Sectionable.class::cast)
            .map(Sectionable::getSection)
            .map(Section::getDisplayName)
            .orElse(null);
    }

    /**
     * Returns the URL to the {@code category} of the content being promoted.
     * <p/>
     * Defaults to the URL of the {@link Section} of the content, if {@link Sectionable}.
     *
     * @param site the {@link Site} to use as the basis of the URL (nullable).
     * @return a plain text {@link String} (optional).
     */
    default String getPromotableCategoryUrl(Site site) {
        return Optional.of(this)
            .filter(Sectionable.class::isInstance)
            .map(Sectionable.class::cast)
            .map(Sectionable::getSection)
            .map(section -> section.getLinkableUrl(site))
            .orElse(null);
    }

    /**
     * Returns the {@code date} of the content being promoted.
     * <p/>
     * Defaults to the {@code publish date} of the content.
     *
     * @return a {@link Date} (optional).
     */
    default Date getPromotableDate() {
        return Optional.ofNullable(this.as(Content.ObjectModification.class))
            .map(Content.ObjectModification::getPublishDate)
            .orElse(null);
    }

    /**
     * Returns the {@code url} used when promoting content internally.
     *
     * @param site the {@link Site} to use as the basis of the URL (nullable).
     * @return an plain text {@link String} containing the canonical URL of the content based on the provided {@link
     * Site} (optional).
     */
    default String getPromotableUrl(Site site) {
        return DirectoryItemUtils.getCanonicalUrl(site, this);
    }

    /**
     * Returns the {@code duration} of the content being promoted.
     *
     * @return a plain text {@link String} (optional).
     */
    default String getPromotableDuration() {
        return null;
    }

    /**
     * Creates a wrapper around this object based on the provided {@link Promo} overrides to be passed to the view
     * system.
     *
     * @param overrides a {@link PromoWrapper} (never null).
     */
    default PromoWrapper createPromoWrapper(Promo overrides) {
        if (overrides == null) {
            throw new IllegalArgumentException("Overrides cannot be null!");
        }
        return new PromoWrapper(this, overrides);
    }
}
