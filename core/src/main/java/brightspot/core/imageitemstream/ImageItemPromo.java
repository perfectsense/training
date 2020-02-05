package brightspot.core.imageitemstream;

import brightspot.core.image.Image;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;

@Recordable.Embedded
@Recordable.PreviewField("item/file")
@Recordable.DisplayName("Image")
public class ImageItemPromo extends Record implements
    ImageItem {

    @Required
    @ToolUi.BulkUpload
    private Image item;

    @ToolUi.Placeholder(dynamicText = "${content.getTitlePlaceholder()}", editable = true)
    private String title;

    @DisplayName("Caption")
    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Placeholder(dynamicText = "${content.getDescriptionPlaceholder()}", editable = true)
    private String description;

    @DisplayName("Credit")
    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Placeholder(dynamicText = "${content.getAttributionPlaceholder()}", editable = true)
    private String attribution;

    /**
     * Return the override title if it is set, otherwise the title placeholder.
     *
     * @return a plain text {@link String} (optional).
     */
    @Override
    public String getImageItemTitle() {
        return ObjectUtils.firstNonBlank(title, getTitlePlaceholder());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Return the override description if it is set, otherwise the description placeholder.
     *
     * @return an inline RichText {@link String} (optional).
     */
    @Override
    public String getImageItemDescription() {
        return ObjectUtils.firstNonBlank(description, getDescriptionPlaceholder());
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return the override attribution if it is set, otherwise the attribution placeholder.
     *
     * @return an inline RichText {@link String} (optional).
     */
    @Override
    public String getImageItemAttribution() {
        return ObjectUtils.firstNonBlank(attribution, getAttributionPlaceholder());
    }

    @Override
    public Image getImageItemImage() {
        return item;
    }

    public void setItem(Image item) {
        this.item = item;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getTitlePlaceholder() {
        return getImageItemImage() != null ? getImageItemImage().getPromotableTitle() : null;
    }

    public String getDescriptionPlaceholder() {
        return getImageItemImage() != null ? getImageItemImage().getPromotableDescription() : null;
    }

    public String getAttributionPlaceholder() {
        return getImageItemImage() != null ? getImageItemImage().getCredit() : null;
    }

    @Override
    public String getLabel() {
        return item != null ? item.getLabel() : null;
    }

    @Override
    public StorageItem getImageItemPreviewImage() {
        return item == null ? null : item.getFile();
    }
}
