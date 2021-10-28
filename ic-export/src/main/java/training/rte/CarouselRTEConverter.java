package training.rte;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import brightspot.core.carousel.CarouselRichTextElement;
import brightspot.core.carousel.ExistingGalleryItemStream;
import brightspot.core.imageitemstream.AdvancedImageItemStream;
import brightspot.core.imageitemstream.ImageItemPromo;
import brightspot.core.imageitemstream.ImageItemStream;
import brightspot.util.FixedMap;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.db.RichTextElement;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.nodes.Element;
import training.ExportUtils;

public class CarouselRTEConverter extends RichTextConverter {

    @Override
    public void convert(Recordable parent, Element element) {
        RichTextElement rte = Objects.requireNonNull(RichTextElement.fromElement(parent, element));
        if (!(rte instanceof CarouselRichTextElement)) {
            throw new IllegalArgumentException("Received " + rte.getClass().getName());
        }

        CarouselRichTextElement carouselRTE = (CarouselRichTextElement) rte;

        Map<String, Object> itemStream = processItemStream(carouselRTE.getItemStream());
        if (itemStream == null) {
            element.remove();
            return;
        }

        Element replacement = new Element("bsp-carousel");
        replacement.attr("data-state", ObjectUtils.toJson(FixedMap.of(
            "titleOverride", StringEscapeUtils.escapeHtml4(carouselRTE.getTitle()),
            "items", itemStream,
            "_id", carouselRTE.getId(),
            "_type", ExportUtils.getExportType(carouselRTE)
        )));

        replacement.text(carouselRTE.getLabel());

        element.replaceWith(replacement);
    }

    /**
     * This needs to stay in sync with {@link training.AdvancedImageItemStreamViewModel}.
     */
    private Map<String, Object> processItemStream(ImageItemStream itemStream) {
        if (itemStream == null) {
            // bad data?
            return null;
        }

        if (itemStream instanceof AdvancedImageItemStream) {
            AdvancedImageItemStream advancedImageItemStream = (AdvancedImageItemStream) itemStream;
            return ImmutableMap.of(
                "items", advancedImageItemStream.getItems()
                    .stream()
                    .map(this::processImageItem)
                    .collect(Collectors.toList()),
                "_id", advancedImageItemStream.getId().toString(),
                "_type", ExportUtils.getExportType(advancedImageItemStream));

        } else if (itemStream instanceof ExistingGalleryItemStream) {
            ExistingGalleryItemStream existingItemStream = (ExistingGalleryItemStream) itemStream;
            return ImmutableMap.of(
                "existing", ExportUtils.buildRef(existingItemStream.getGallery()),
                "_id", existingItemStream.getId().toString(),
                "_type", ExportUtils.getExportType(existingItemStream));

        } else {
            throw new IllegalArgumentException("Unhandled ImageItemStream type " + itemStream.getClass().getName());
        }
    }

    /**
     * This needs to stay in sync with {@link training.ImageItemPromoViewModel}.
     */
    private Map<String, Object> processImageItem(ImageItemPromo imageItem) {
        return FixedMap.of(
            "item",
            ExportUtils.buildRef(imageItem.getImageItemImage()),
            "webImage.altTextOverride",
            imageItem.getState().get("title"),
            "webImage.captionOverride",
            ExportUtils.processRichText(imageItem, i -> ObjectUtils.to(String.class, i.getState().get("description"))),
            "webImage.creditOverride",
            ExportUtils.processRichText(imageItem, i -> ObjectUtils.to(String.class, i.getState().get("attribution"))),
            "_id",
            imageItem.getId().toString(),
            "_type",
            ExportUtils.getExportType(imageItem));
    }
}
