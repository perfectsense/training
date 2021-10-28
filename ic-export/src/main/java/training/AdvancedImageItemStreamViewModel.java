package training;

import brightspot.core.imageitemstream.AdvancedImageItemStream;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;

/**
 * See also {@link training.rte.CarouselRTEConverter} which also produces this content type.
 */
@JsonView
@ViewInterface
public class AdvancedImageItemStreamViewModel extends ExportViewModel<AdvancedImageItemStream> {

    @ViewKey("items")
    public Iterable<ImageItemPromoViewModel> getItems() {
        return createViews(ImageItemPromoViewModel.class, model.getItems());
    }
}
