package brightspot.core.carousel;

import java.util.Optional;

import brightspot.core.image.Image;
import brightspot.core.image.ImageOption;
import brightspot.core.image.SharedImageOption;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Deprecated
@Recordable.Embedded
@Recordable.DisplayName("Image")
public class ImageCarouselSlide extends Record implements CarouselSlide {

    private ImageOption image;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Placeholder(dynamicText = "${content.captionFallback}", editable = true)
    private String caption;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Placeholder(dynamicText = "${content.creditFallback}", editable = true)
    private String credit;

    public ImageOption getImage() {
        return image;
    }

    public void setImage(ImageOption image) {
        this.image = image;
    }

    public String getCaption() {

        if (ObjectUtils.isBlank(caption)) {
            return getCaptionFallback();
        }
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCredit() {

        if (ObjectUtils.isBlank(credit)) {
            return getCreditFallback();
        }
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getCaptionFallback() {
        return Optional.ofNullable(image)
            .filter(SharedImageOption.class::isInstance)
            .map(SharedImageOption.class::cast)
            .map(SharedImageOption::getImage)
            .map(Image::getCaption)
            .orElse(null);
    }

    public String getCreditFallback() {
        return Optional.ofNullable(image)
            .filter(SharedImageOption.class::isInstance)
            .map(SharedImageOption.class::cast)
            .map(SharedImageOption::getImage)
            .map(Image::getCredit)
            .orElse(null);
    }

    @Override
    public ImageOption getPreviewImage() {
        return getImage();
    }
}
