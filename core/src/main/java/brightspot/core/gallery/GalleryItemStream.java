package brightspot.core.gallery;

import brightspot.core.imageitemstream.DynamicImageItemStream;
import brightspot.core.imageitemstream.ImageItemStream;
import brightspot.core.imageitemstream.SimpleImageItemStream;
import brightspot.core.tool.DefaultImplementationSupplier;

public interface GalleryItemStream extends ImageItemStream {

    static GalleryItemStream createDefault() {
        return DefaultImplementationSupplier.createDefault(GalleryItemStream.class, SimpleImageItemStream.class);
    }

    static GalleryItemStream createDynamic() {
        return new DynamicImageItemStream();
    }
}
