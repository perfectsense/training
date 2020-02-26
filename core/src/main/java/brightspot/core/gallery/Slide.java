package brightspot.core.gallery;

import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;

@Deprecated
@Recordable.Embedded
@Recordable.PreviewField("content/getGallerySlideContentImageFile()")
public class Slide extends Record {

    @Required
    @ToolUi.BulkUpload
    private SlideContent content;

    @ToolUi.Placeholder(dynamicText = "${content.titleFallback}", editable = true)
    private String title;

    @ToolUi.Placeholder(dynamicText = "${content.descriptionFallback}", editable = true)
    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    public String getTitle() {
        if (StringUtils.isBlank(title)) {
            return getTitleFallback();
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        if (StringUtils.isBlank(description)) {
            return getDescriptionFallback();
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SlideContent getContent() {
        return content;
    }

    public void setContent(SlideContent content) {
        this.content = content;
    }

    public String getTitleFallback() {
        return content == null ? null : content.getGallerySlideContentTitle();
    }

    public String getDescriptionFallback() {
        return content == null ? null : content.getGallerySlideContentDescription();
    }

    public StorageItem getGallerySlideItemImageFile() {
        return content == null ? null : content.getGallerySlideContentImageFile();
    }

    @Override
    public String getLabel() {
        if (!StringUtils.isBlank(getTitle())) {
            return getTitle();
        }

        if (content != null) {
            return content.getGallerySlideContentTitle();
        }

        return super.getLabel();
    }
}
