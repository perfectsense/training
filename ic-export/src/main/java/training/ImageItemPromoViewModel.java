package training;

import brightspot.core.imageitemstream.ImageItemPromo;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;
import com.psddev.dari.util.ObjectUtils;

/**
 * See also {@link training.rte.CarouselRTEConverter} which also produces this content type.
 */
@JsonView
@ViewInterface
public class ImageItemPromoViewModel extends ExportViewModel<ImageItemPromo> {

    @ViewKey("item")
    public RefView getItem() {
        return createView(RefView.class, model.getImageItemImage());
    }

    @ViewKey("webImage.altTextOverride")
    public String getAltTextOverride() {
        return ObjectUtils.to(String.class, model.getState().get("title"));
    }

    @ViewKey("webImage.captionOverride")
    public CharSequence getCaptionOverride() {
        return ExportUtils.processRichText(model, i -> ObjectUtils.to(String.class, i.getState().get("description")));
    }

    @ViewKey("webImage.creditOverride")
    public CharSequence getCreditOverride() {
        return ExportUtils.processRichText(model, i -> ObjectUtils.to(String.class, i.getState().get("attribution")));
    }
}
