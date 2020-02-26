package brightspot.core.carousel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.gallery.Gallery;
import brightspot.core.imageitemstream.AdvancedImageItemStream;
import brightspot.core.imageitemstream.CarouselSlideToImageItemPromo;
import brightspot.core.imageitemstream.ImageItem;
import brightspot.core.imageitemstream.ImageItemPromo;
import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Recordable.DisplayName("Gallery")
@RichTextElement.Tag(value = CarouselRichTextElement.TAG_NAME,
    block = true,
    initialBody = "Gallery",
    preview = true,
    position = -40.0,
    readOnly = true,
    root = true,
    keymaps = { "Shift-Ctrl-G" },
    tooltip = "Add Gallery (Shift-Ctrl-G)",
    menu = "Enhancements"
)
@ToolUi.IconName(Gallery.ICON_NAME)
public class CarouselRichTextElement extends RichTextElement {

    public static final String TAG_NAME = "bsp-carousel";

    private static final String STATE_ATTRIBUTE = "data-state";
    private static final Logger LOGGER = LoggerFactory.getLogger(CarouselRichTextElement.class);

    @ToolUi.Placeholder(dynamicText = "${content.getTitleFallback()}")
    private String title;

    @Embedded
    @Required
    private CarouselItemStream items = CarouselItemStream.createDefault();

    @Deprecated
    @Embedded
    List<CarouselSlide> slides;

    public String getTitle() {

        if (ObjectUtils.isBlank(title)) {
            return getTitleFallback();
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CarouselItemStream getItemStream() {

        replaceSlidesWithItemStream(true);
        return items;
    }

    public void setItemStream(CarouselItemStream items) {
        this.items = items;
    }

    public List<CarouselSlide> getSlides() {
        if (slides == null) {
            slides = new ArrayList<>();
        }
        return slides;
    }

    public void setSlides(List<CarouselSlide> slides) {
        this.slides = slides;
    }

    public String getTitleFallback() {

        return Optional.ofNullable(getItemStream())
            .map(CarouselItemStream::getTitlePlaceholder)
            .orElse(null);
    }

    @Override
    public void fromAttributes(Map<String, String> attributes) {
        if (attributes != null) {
            String stateString = attributes.get(STATE_ATTRIBUTE);
            if (stateString != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> simpleValues = (Map<String, Object>) ObjectUtils.fromJson(stateString);
                getState().setValues(simpleValues);
                replaceSlidesWithItemStream(true);
            }
        }
    }

    @Override
    public Map<String, String> toAttributes() {

        Map<String, String> attributes = new LinkedHashMap<>();
        replaceSlidesWithItemStream(true);
        attributes.put(STATE_ATTRIBUTE, ObjectUtils.toJson(getState().getSimpleValues()));
        return attributes;
    }

    @Override
    public void fromBody(String body) {
        // do nothing
    }

    @Override
    public String toBody() {
        return getLabel();
    }

    @Override
    public void writePreviewHtml(ToolPageContext page)
        throws IOException {

        if (!ObjectUtils.isBlank(getItemStream())) {
            StorageItem previewImage = getItemStream().getItems(getOwner(), this, 0, 1).stream()
                .map(ImageItem::getImageItemPreviewImage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);

            if (previewImage != null) {
                page.writeStart("img", "src", previewImage.getPublicUrl(), "height", 300);
            }
        }
    }

    @Override
    public String getLabel() {
        return ObjectUtils.firstNonBlank(getTitle(), "Untitled");
    }

    @Override
    protected void beforeSave() {

        super.beforeSave();
        replaceSlidesWithItemStream(false);
    }

    private void replaceSlidesWithItemStream(boolean saveImmediately) {

        if (items == null) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Did not find items! Creating new default item stream!");
            }

            items = CarouselItemStream.createDefault();
        }

        if (slides != null && !slides.isEmpty()) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Found existing slide deck in Carousel! Replacing {} slides!", slides.size());
            }

            // converting Slides to GalleryItemPromos
            CarouselSlideToImageItemPromo adapter = new CarouselSlideToImageItemPromo();
            List<ImageItemPromo> promos = slides.stream()
                .map(adapter::adapt)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Size of replacement list:  {} ImageItemPromos!", promos.size());
            }

            // Creating replacement for existing itemStream
            AdvancedImageItemStream imageItemStream = new AdvancedImageItemStream();
            imageItemStream.getItems().addAll(promos);

            // Setting itemStream
            this.setItemStream(imageItemStream);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Clearing slides!");
            }
            // clearing
            slides.clear();
            slides = null;
            if (saveImmediately) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Saving changes immediately!");
                }
                saveImmediately();
            }
        }
    }

    private Site getOwner() {

        Site.ObjectModification siteData = this.as(Site.ObjectModification.class);
        return siteData != null ? siteData.getOwner() : null;
    }
}
