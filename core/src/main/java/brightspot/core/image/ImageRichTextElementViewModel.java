package brightspot.core.image;

import java.util.Map;
import java.util.Optional;

import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.image.ImageSize;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.figure.FigureView;

public class ImageRichTextElementViewModel
    extends ViewModel<ImageRichTextElement>
    implements FigureView {

    @Override
    public boolean shouldCreate() {
        ImageOption image = model.getImage();
        return image != null && image.getImageOptionFile() != null;
    }

    @Override
    public Map<String, ?> getImage() {
        return Optional.ofNullable(model.getImage())
            .map(image -> ImageSize.getAttributes(image.getImageOptionFile()))
            .orElse(null);
    }

    @Override
    public CharSequence getAlt() {
        return Optional.ofNullable(model.getImage())
            .map(ImageOption::getImageOptionAltText)
            .orElse(null);
    }

    @Override
    public CharSequence getCaption() {
        return Optional.ofNullable(model.getImage())
            .filter(SharedImageOption.class::isInstance)
            .map(SharedImageOption.class::cast)
            .map(SharedImageOption::getImage)
            .map(Image::getCaption)
            .map(caption -> RichTextUtils.buildInlineHtml(model.getState().getDatabase(), caption, this::createView))
            .orElse(null); // One-off images do not have captions
    }

    @Override
    public CharSequence getCredit() {
        return Optional.ofNullable(model.getImage())
            .filter(SharedImageOption.class::isInstance)
            .map(SharedImageOption.class::cast)
            .map(SharedImageOption::getImage)
            .map(Image::getCredit)
            .map(credit -> RichTextUtils.buildInlineHtml(model.getState().getDatabase(), credit, this::createView))
            .orElse(null); // One-off images do not have credits
    }
}
