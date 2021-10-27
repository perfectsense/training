package brightspot.imageitemstream;

import brightspot.util.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.gallery.GallerySlideView;
import com.psddev.styleguide.gallery.GallerySlideViewInfoAttributionField;
import com.psddev.styleguide.gallery.GallerySlideViewInfoDescriptionField;
import com.psddev.styleguide.gallery.GallerySlideViewInfoTitleField;
import com.psddev.styleguide.gallery.GallerySlideViewMediaContentField;

public class WebImageItemSlideViewModel extends ViewModel<WebImageItem> implements GallerySlideView {

    @Override
    public Iterable<? extends GallerySlideViewInfoTitleField> getInfoTitle() {
        return RichTextUtils.buildInlineHtml(
                model,
                WebImageItem::getTitle,
                e -> createView(GallerySlideViewInfoTitleField.class, e));
    }

    @Override
    public Iterable<? extends GallerySlideViewInfoAttributionField> getInfoAttribution() {
        return RichTextUtils.buildHtml(
                model,
                WebImageItem::getWebImageCredit,
                e -> createView(GallerySlideViewInfoAttributionField.class, e));
    }

    @Override
    public Iterable<? extends GallerySlideViewInfoDescriptionField> getInfoDescription() {
        return RichTextUtils.buildHtml(
                model,
                WebImageItem::getWebImageCaption,
                e -> createView(GallerySlideViewInfoDescriptionField.class, e));
    }

    @Override
    public Iterable<? extends GallerySlideViewMediaContentField> getMediaContent() {
        return createViews(GallerySlideViewMediaContentField.class, model.getItem());
    }
}
