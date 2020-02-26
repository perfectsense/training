package brightspot.core.imageitemstream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import brightspot.core.carousel.CarouselItemStream;
import brightspot.core.gallery.GalleryItemStream;
import brightspot.core.image.Image;
import brightspot.core.listmodule.AbstractListItemStream;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Advanced")
public class AdvancedImageItemStream extends AbstractListItemStream implements
    CarouselItemStream,
    GalleryItemStream,
    ImageItemStream,
    Interchangeable {

    @ToolUi.Unlabeled
    private List<ImageItemPromo> items;

    @Override
    public List<ImageItemPromo> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;

    }

    @Override
    public List<ImageItemPromo> getItems(Site site, Object mainObject, long offset, int limit) {

        long toIndex = limit + offset;
        long count = getCount(site, mainObject);

        if (toIndex >= count) {
            toIndex = count;
        }

        return getItems().subList(Math.toIntExact(offset), Math.toIntExact(toIndex));
    }

    @Override
    public int getItemsPerPage(Site site, Object mainObject) {
        return getItems().size();
    }

    @Override
    public boolean loadTo(Object newObj) {
        if (newObj instanceof DynamicImageItemStream) {
            ((DynamicImageItemStream) newObj).getPinnedItems().addAll(getItems());
            return true;
        }
        if (newObj instanceof SimpleImageItemStream) {
            ((SimpleImageItemStream) newObj).getItems().addAll(getImages());

            return true;
        }
        return false;
    }

    private List<Image> getImages() {
        return getItems().stream()
            .map(ImageItemPromo::getImageItemImage)
            .collect(Collectors.toList());
    }
}
