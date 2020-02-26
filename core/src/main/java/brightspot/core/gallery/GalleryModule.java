package brightspot.core.gallery;

import brightspot.core.carousel.CarouselItemStream;
import brightspot.core.listmodule.AbstractListModule;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Gallery")
public class GalleryModule extends AbstractListModule {

    @ToolUi.Placeholder(dynamicText = "${content.getTitleFallback()}")
    private String title;

    @Embedded
    @Required
    private CarouselItemStream items = CarouselItemStream.createDefault();

    /**
     * Returns the {@code title}.
     *
     * @return a plain text {@link String} (optional).
     */
    @Override
    public String getTitle() {

        return title;
    }

    /**
     * Sets the {@code title}.
     *
     * @@param title a plain text {@link String} (optional).
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the item stream.
     *
     * @return a {@link CarouselItemStream} (required).
     */
    @Override
    public CarouselItemStream getItemStream() {

        return items;
    }

    /**
     * Sets the item stream.
     *
     * @param items a {@link CarouselItemStream} (required).
     */
    public void setItemStream(CarouselItemStream items) {
        this.items = items;
    }
}
