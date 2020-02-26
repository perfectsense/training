package brightspot.core.gallery;

import java.util.Optional;

import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.carousel.CarouselView;
import com.psddev.styleguide.core.carousel.CarouselViewSlidesField;

public class GalleryModuleViewModel extends ViewModel<GalleryModule> implements CarouselView {

    @CurrentSite
    private Site site;

    @Override
    public CharSequence getTitle() {
        // plain text
        return model.getTitle();
    }

    @Override
    public Iterable<? extends CarouselViewSlidesField> getSlides() {

        // return all items
        return createViews(CarouselViewSlidesField.class, Optional.ofNullable(model.getItemStream())
            .map(carouselItemStream -> carouselItemStream.getItems(
                site,
                model,
                0,
                carouselItemStream.getItemsPerPage(site, model)))
            .orElse(null));
    }
}
