package brightspot.core.imageitemstream;

import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.gallery.GallerySlideView;
import com.psddev.styleguide.core.gallery.GallerySlideViewMediaContentField;

public class ImageItemSlideViewModel extends ViewModel<ImageItem> implements GallerySlideView {

    @Override
    public CharSequence getInfoTitle() {
        // plain text
        return model.getImageItemTitle();
    }

    @Override
    public CharSequence getInfoDescription() {
        // inline RichText
        return RichTextUtils.buildInlineHtml(
            model.getState().getDatabase(),
            model.getImageItemDescription(),
            this::createView);
    }

    @Override
    public CharSequence getInfoAttribution() {

        // inline RichText
        return RichTextUtils.buildInlineHtml(
            model.getState().getDatabase(),
            model.getImageItemAttribution(),
            this::createView);
    }

    @Override
    public Iterable<? extends GallerySlideViewMediaContentField> getMediaContent() {
        return createViews(GallerySlideViewMediaContentField.class, model.getImageItemImage());
    }
}
