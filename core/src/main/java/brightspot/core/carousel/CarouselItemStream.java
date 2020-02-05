package brightspot.core.carousel;

import brightspot.core.imageitemstream.DynamicImageItemStream;
import brightspot.core.imageitemstream.ImageItemStream;
import brightspot.core.imageitemstream.SimpleImageItemStream;
import brightspot.core.tool.DefaultImplementationSupplier;

public interface CarouselItemStream extends ImageItemStream {

    static CarouselItemStream createDefault() {
        return DefaultImplementationSupplier.createDefault(CarouselItemStream.class, SimpleImageItemStream.class);
    }

    static CarouselItemStream createDynamic() {
        return new DynamicImageItemStream();
    }
}
