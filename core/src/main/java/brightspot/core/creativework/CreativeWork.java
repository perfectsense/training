package brightspot.core.creativework;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.Localization;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ConditionallyValidatable;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;

@Recordable.LabelFields("headline")
@Seo.TitleFields("getSeoTitle")
@Seo.DescriptionFields("getSeoDescription")
public abstract class CreativeWork extends Content implements ConditionallyValidatable {

    public static final String HEADLINE_FIELD = "getHeadline";

    @Required
    @ToolUi.DisplayFirst
    @ToolUi.Placeholder(dynamicText = "${content.getHeadlineFallbackOrRequired()}")
    private String headline;

    @ToolUi.Placeholder(dynamicText = "${content.getSubHeadlineFallback()}", editable = true)
    private String subHeadline;

    /**
     * Returns the {@code headline} or "title".
     *
     * @return a plain text {@link String} (required).
     */
    @Indexed
    @ToolUi.Hidden
    public String getHeadline() {
        return !ObjectUtils.isBlank(headline)
            ? headline
            : getHeadlineFallback();
    }

    /**
     * Sets the {@code headline} or "title".
     *
     * @param headline a plain text {@link String} (required).
     */
    public void setHeadline(String headline) {
        this.headline = headline;
    }

    /**
     * Returns the {@code subHeadline} or "description".
     *
     * @return a plain text {@link String} (optional).
     */
    @Indexed
    @ToolUi.Hidden
    public String getSubHeadline() {
        return !ObjectUtils.isBlank(subHeadline)
            ? subHeadline
            : getSubHeadlineFallback();
    }

    /**
     * Sets the {@code subHeadline} or "description".
     *
     * @param subHeadline a plain text {@link String} (optional).
     */
    public void setSubHeadline(String subHeadline) {
        this.subHeadline = subHeadline;
    }

    /**
     * Returns the {@code headline} or "title" used for SEO.
     *
     * @return a plain text {@link String} (required).
     */
    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoTitle() {
        return getHeadline();
    }

    /**
     * Returns the {@code subHeadline} or "description" used for SEO.
     *
     * @return a plain text {@link String} (optional).
     */
    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoDescription() {
        return getSubHeadline();
    }

    /**
     * Returns a fallback value for the {@code headline} or "title".
     *
     * @return a plain text {@link String} (optional).
     */
    public String getHeadlineFallback() {
        return null;
    }

    /**
     * Returns a fallback value for the {@code subHeadline} or "description".
     *
     * @return a plain text {@link String} (optional).
     */
    public String getSubHeadlineFallback() {
        return null;
    }

    @Override
    public boolean shouldValidate(ObjectField objectField) {
        return !("headline".equals(objectField.getInternalName())
            && !ObjectUtils.isBlank(getHeadlineFallback()));
    }

    /**
     * Returns a fallback value for the {@code headline} or "title" based on {@link CreativeWork#getHeadlineFallback()}.
     * Returns "(Required)" if the original fallback value is blank.
     * <p/>
     * <strong>Note:</strong> for internal use only!
     *
     * @return a plain text {@link String} (never null).
     */
    public String getHeadlineFallbackOrRequired() {
        String fallback = getHeadlineFallback();
        return !StringUtils.isBlank(fallback)
            ? fallback
            : Localization.text(getCurrentToolUser().getLocale(), this, "placeholder.required");
    }
}
