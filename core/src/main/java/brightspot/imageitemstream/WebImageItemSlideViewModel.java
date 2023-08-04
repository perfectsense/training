package brightspot.imageitemstream;

import brightspot.util.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.gallery.GallerySlideView;
import com.psddev.styleguide.gallery.GallerySlideViewCaptionField;
import com.psddev.styleguide.gallery.GallerySlideViewCreditField;
import com.psddev.styleguide.gallery.GallerySlideViewMediaField;
import com.psddev.styleguide.gallery.GallerySlideViewTitleField;

public class WebImageItemSlideViewModel extends ViewModel<WebImageItem> implements GallerySlideView {

    @Override
    public Iterable<? extends GallerySlideViewTitleField> getTitle() {
        return RichTextUtils.buildInlineHtml(
            model,
            WebImageItem::getTitle,
            e -> createView(GallerySlideViewTitleField.class, e));
    }

    @Override
    public Iterable<? extends GallerySlideViewCreditField> getCredit() {
        return RichTextUtils.buildHtml(
            model,
            WebImageItem::getWebImageCredit,
            e -> createView(GallerySlideViewCreditField.class, e));
    }

    @Override
    public Iterable<? extends GallerySlideViewCaptionField> getCaption() {
        return RichTextUtils.buildHtml(
            model,
            WebImageItem::getWebImageCaption,
            e -> createView(GallerySlideViewCaptionField.class, e));
    }

    @Override
    public Iterable<? extends GallerySlideViewMediaField> getMedia() {
        return createViews(GallerySlideViewMediaField.class, model.getItem());
    }
}
