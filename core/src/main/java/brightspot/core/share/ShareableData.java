package brightspot.core.share;

import brightspot.core.image.ImageOption;
import brightspot.core.tool.ImagePreviewHtml;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;

@Recordable.FieldInternalNamePrefix("shareable.")
public class ShareableData extends Modification<Shareable> implements ImagePreviewHtml {

    private static final String SHARE_TAB = "Overrides";
    protected static final String SHARING_OVERRIDES_CLUSTER_NAME = "Sharing Overrides (External)";

    @ToolUi.Cluster(SHARING_OVERRIDES_CLUSTER_NAME)
    @ToolUi.Placeholder(dynamicText = "${content.getShareableTitleFallback()}", editable = true)
    @ToolUi.Tab(SHARE_TAB)
    private String shareTitle;

    @ToolUi.Cluster(SHARING_OVERRIDES_CLUSTER_NAME)
    @ToolUi.Placeholder(dynamicText = "${content.getShareableDescriptionFallback()}", editable = true)
    @ToolUi.Tab(SHARE_TAB)
    private String shareDescription;

    @ToolUi.Cluster(SHARING_OVERRIDES_CLUSTER_NAME)
    @ToolUi.Tab(SHARE_TAB)
    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getShareableImagePlaceholderHtml()}'></span>")
    private ImageOption shareImage;

    /**
     * Returns the {@code title} or "headline" used when sharing content externally.
     * <p/>
     * Falls back to the value of {@link Shareable#getShareableTitleFallback()}.
     *
     * @return a plain text {@link String} (optional).
     */
    public String getShareTitle() {
        if (StringUtils.isBlank(shareTitle)) {
            return getOriginalObject().getShareableTitleFallback();
        }
        return shareTitle;
    }

    /**
     * Sets the {@code title} or "headline" used when sharing content externally.
     *
     * @param shareTitle a plain text {@link String} (optional).
     */
    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    /**
     * Returns the {@code description} or "subHeadline" used when sharing content externally.
     * <p/>
     * Falls back to the value of {@link Shareable#getShareableDescriptionFallback()}.
     *
     * @return a plain text {@link String} (optional).
     */
    public String getShareDescription() {
        if (StringUtils.isBlank(shareDescription)) {
            return getOriginalObject().getShareableDescriptionFallback();
        }
        return shareDescription;
    }

    /**
     * Sets the {@code description} or "subHeadline" used when sharing content externally.
     *
     * @param shareDescription a plain text {@link String} (optional).
     */
    public void setShareDescription(String shareDescription) {
        this.shareDescription = shareDescription;
    }

    /**
     * Returns the {@code image} used when sharing content externally.
     * <p/>
     * Falls back to the value of {@link Shareable#getShareableImageFallback()}.
     *
     * @return an {@link ImageOption} (optional).
     */
    public ImageOption getShareImage() {

        return shareImage != null
            ? shareImage
            : getOriginalObject().getShareableImageFallback();
    }

    /**
     * Sets the {@code shareImage} used when sharing content externally.
     *
     * @param shareImage an {@link ImageOption} (optional).
     */
    public void setShareImage(ImageOption shareImage) {
        this.shareImage = shareImage;
    }

    /**
     * Returns the placeholder for the {@code image}, using the image returned by {@link
     * Shareable#getShareableImageFallback()}.
     * <p/>
     * <strong>Note:</strong> For internal use only!
     *
     * @return a HTML {@link String} (optional).
     */
    public String getShareableImagePlaceholderHtml() {

        if (shareImage != null) {
            return null;
        }

        return writePreviewImageHtml(getOriginalObject().getShareableImageFallback());
    }
}
