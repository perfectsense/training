package brightspot.core.carousel;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import brightspot.core.gallery.Gallery;
import brightspot.core.gallery.GalleryItemStream;
import brightspot.core.imageitemstream.ImageItem;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
@Recordable.DisplayName("Use Existing Gallery")
public class ExistingGalleryItemStream extends Record implements CarouselItemStream {

    @Required
    private Gallery gallery;

    public Gallery getGallery() {
        return gallery;
    }

    public void setItems(Gallery gallery) {
        this.gallery = gallery;
    }

    @Override
    public List<? extends ImageItem> getItems(Site site, Object mainObject, long offset, int limit) {
        return getGalleryItemStream()
            .map(galleryItemStream -> galleryItemStream.getItems(site, mainObject, offset, limit))
            .orElse(Collections.emptyList());
    }

    @Override
    public long getCount(Site site, Object mainObject) {
        return getGalleryItemStream()
            .map(galleryItemStream -> galleryItemStream.getCount(site, mainObject))
            .orElse(0L);
    }

    @Override
    public boolean hasMoreThan(Site site, Object mainObject, long count) {
        return getGalleryItemStream()
            .map(galleryItemStream -> galleryItemStream.hasMoreThan(site, mainObject, count))
            .orElse(false);
    }

    @Override
    public int getItemsPerPage(Site site, Object mainObject) {
        return getGalleryItemStream()
            .map(galleryItemStream -> galleryItemStream.getItemsPerPage(site, mainObject))
            .orElse(0);
    }

    private Optional<GalleryItemStream> getGalleryItemStream() {
        return Optional.ofNullable(getGallery())
            .map(Gallery::getItemStream);
    }

    @Override
    public String getTitlePlaceholder() {
        return Optional.ofNullable(getGallery())
            .map(Gallery::getPromotableTitle)
            .orElse(null);
    }
}
