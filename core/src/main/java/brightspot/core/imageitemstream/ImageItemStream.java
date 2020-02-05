package brightspot.core.imageitemstream;

import java.util.List;

import brightspot.core.listmodule.ItemStream;
import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.cms.db.Site;

public interface ImageItemStream extends ItemStream {

    @Override
    List<? extends ImageItem> getItems(Site site, Object mainObject, long offset, int limit);

    static ImageItemStream createDefault() {
        return DefaultImplementationSupplier.createDefault(ImageItemStream.class, SimpleImageItemStream.class);
    }

    static ImageItemStream createDynamic() {
        return new DynamicImageItemStream();
    }
}
