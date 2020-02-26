package brightspot.core.tool;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

import brightspot.core.image.ImageOption;
import brightspot.core.image.ImageRichTextElement;
import brightspot.core.image.OneOffImageOption;
import brightspot.core.quote.QuoteRichTextElement;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ImageMetadataMap;
import com.psddev.dari.util.IoUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StorageItemPathGenerator;
import com.psddev.dari.util.StringUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to convert generic html into a format suitable for the Brightspot Rich Text editor.
 *
 * Each tag type is processed by a separate method so the conversion can be customized.
 */
public class RteImporter {

    public static final String IMAGE_INGESTED_DATE = "bex.rteImporter.imageIngestedDate";
    public static final String IMAGE_INGESTED_URL = "bex.rteImporter.imageIngestedUrl";

    private static final Logger LOGGER = LoggerFactory.getLogger(RteImporter.class);

    private static final StorageItemPathGenerator PATH_GENERATOR = new UrlHashStorageItemPathGenerator();
    private static final String DATA_ATTR = "data-state";

    private final Whitelist whitelist;

    /**
     * Construct a RteImporter which will not perform any cleaning on input html.
     */
    public RteImporter() {
        this(null);
    }

    /**
     * Construct a RteImporter which will use the given whitelist to clean input html.
     *
     * @param whitelist a {@link Whitelist} which will be used to clean the raw html before parsing it; may be null, in
     * which case no cleaning will be performed
     */
    public RteImporter(Whitelist whitelist) {
        this.whitelist = whitelist;
    }

    /**
     * Convert an html string to a string suitable for a RichText field.
     *
     * @param html the raw html to convert
     * @param sourceUrl the url the html was obtained from (used for converting relative hrefs to absolute); may be
     * null
     * @return a String suitable for a {@link com.psddev.cms.db.ToolUi.RichText} field, or null if {@code html} is blank
     */
    public String convert(String html, String sourceUrl) {
        if (StringUtils.isBlank(html)) {
            return null;
        }

        Element docBody = parseHtml(html, sourceUrl);
        processElements(docBody);
        return docBody.html();
    }

    /**
     * Parse the given html into a DOM tree using Jsoup, using {@code whitelist} to clean it.
     *
     * @param html the raw html to convert
     * @param sourceUrl the url the html was obtained from (used for converting relative hrefs to absolute); may be
     * null
     * @return an {@link Element} containing the root of the html document, cleaned and ready to be processed
     */
    public Element parseHtml(String html, String sourceUrl) {
        Document doc = Jsoup.parseBodyFragment(html, sourceUrl != null ? sourceUrl : "");

        if (whitelist != null) {
            doc = new Cleaner(whitelist).clean(doc);
        }

        doc.outputSettings(new Document.OutputSettings()
            .indentAmount(0)
            .prettyPrint(false));

        return doc.body();
    }

    /**
     * Process each tag type.
     *
     * @param docBody the root node to process
     */
    public void processElements(Element docBody) {

        // inline
        processEm(docBody.getElementsByTag("em"));
        processStrong(docBody.getElementsByTag("strong"));
        processImg(docBody.getElementsByTag("img"));
        processSpans(docBody.getElementsByTag("span"));

        // block
        processBlockquotes(docBody.getElementsByTag("blockquote"));
        processDivs(docBody.getElementsByTag("div"));
        processParagraphs(docBody.getElementsByTag("p"));
    }

    /**
     * Create an ImageOption for an ImageRichTextElement.
     *
     * By default, creates a {@link OneOffImageOption}, storing the image data using {@link
     * #createImageStorageItem(URL)}.
     *
     * @param imageUrl the url of the image to fetch; not blank
     * @param altText the alt text of the image; may be null
     * @return an {@link ImageOption}; never null
     * @throws IllegalArgumentException if the given {@code imageUrl} is blank
     * @throws IOException if an error occurs while saving the image data
     */
    public ImageOption createImageOption(String imageUrl, String altText) throws IOException {
        if (StringUtils.isBlank(imageUrl)) {
            throw new IllegalArgumentException("Image url cannot be blank");
        }
        OneOffImageOption imageOption = new OneOffImageOption();
        imageOption.setAltText(!StringUtils.isBlank(altText) ? altText : null);
        imageOption.setFile(createImageStorageItem(new URL(imageUrl)));

        // store metadata in state
        State imageState = imageOption.getState();
        imageState.put(IMAGE_INGESTED_URL, imageUrl);
        imageState.put(IMAGE_INGESTED_DATE, new Date());

        return imageOption;
    }

    /**
     * Fetch an image and create a StorageItem for it.
     *
     * @param url the url of the image to fetch; not null
     * @return a {@link StorageItem} containing the data for the image; never null
     * @throws IOException if an error occurs while saving the image data
     * @throws IllegalArgumentException if the given {@code url} has a non-image MIME type
     */
    public StorageItem createImageStorageItem(URL url) throws IOException {
        String mimeType = ObjectUtils.getContentType(url.getPath());
        if (!mimeType.startsWith("image/")) {
            // Image#file and OneOffImageOption#file require image/* MIME type
            throw new IllegalArgumentException("Invalid MIME type '" + mimeType + "' for image: " + url.toString());
        }

        StorageItem storageItem = StorageItem.Static.create();
        storageItem.setContentType(mimeType);
        storageItem.setPath(PATH_GENERATOR.createPath(url.toString()));

        byte[] imageData;
        if (!storageItem.isInStorage()) {
            imageData = IoUtils.toByteArray(url);
            storageItem.setData(new ByteArrayInputStream(imageData));
            storageItem.save();
        } else {
            try (InputStream is = storageItem.getData()) {
                imageData = IOUtils.toByteArray(is);
            }
        }
        storageItem.getMetadata().putAll(new ImageMetadataMap(new ByteArrayInputStream(imageData)));
        return storageItem;
    }

    /**
     * Process {@code <blockquote>} tags.
     *
     * By default, converts them to {@link QuoteRichTextElement}, stripping their internal html, if any.
     */
    public void processBlockquotes(Elements blockquotes) {
        if (!ObjectUtils.isBlank(blockquotes)) {
            for (Element blockquote : blockquotes) {
                if (blockquote.hasText() || !blockquote.children().isEmpty()) {
                    QuoteRichTextElement quoteRTE = new QuoteRichTextElement();
                    quoteRTE.setQuote(blockquote.text());
                    if (blockquote.hasAttr("cite")) {
                        quoteRTE.setAttribution(blockquote.attr("cite"));
                    }

                    Element bspQuote = new Element(Tag.valueOf(QuoteRichTextElement.TAG_NAME), "");
                    bspQuote.attr(DATA_ATTR, quoteRTE.toAttributes().get(DATA_ATTR));
                    bspQuote.text(quoteRTE.toBody());
                    blockquote.replaceWith(bspQuote);
                } else {
                    blockquote.remove();
                }
            }
        }
    }

    /**
     * Process {@code <div>} tags.
     *
     * By default, converts "<div>some text </div>" to "some text <br>".
     */
    public void processDivs(Elements divs) {
        if (!ObjectUtils.isBlank(divs)) {
            for (Element div : divs) {
                if ((div.hasText() || !div.children().isEmpty())) {
                    div.appendChild(new Element(Tag.valueOf("br"), ""));
                    div.unwrap();
                } else {
                    div.remove();
                }
            }
        }
    }

    /**
     * Process {@code <em>} tags.
     *
     * By default, replaces them with {@code <i>} tags.
     */
    public void processEm(Elements emElements) {
        if (!ObjectUtils.isBlank(emElements)) {
            emElements.tagName("i");
        }
    }

    /**
     * Process {@code <img>} tags.
     *
     * By default, imports the image using {@link #createImageOption(String, String)} and converts the tag to an {@link
     * ImageRichTextElement}.
     */
    public void processImg(Elements imgElements) {
        if (!ObjectUtils.isBlank(imgElements)) {
            for (Element img : imgElements) {
                try {
                    ImageRichTextElement imageRTE = new ImageRichTextElement();
                    imageRTE.setImage(createImageOption(img.attr("abs:src"), img.attr("alt")));

                    Element bspImage = new Element(Tag.valueOf(ImageRichTextElement.TAG_NAME), "");
                    bspImage.attr(DATA_ATTR, imageRTE.toAttributes().get(DATA_ATTR));
                    bspImage.text(imageRTE.toBody());
                    img.replaceWith(bspImage);
                } catch (Exception e) {
                    LOGGER.error("Failed to fetch image {} due to {}", img.attr("abs:src"), e.getMessage());
                    img.remove();
                }
            }
        }
    }

    /**
     * Process {@code <p>} tags.
     *
     * By default, converts "<p>some text </p>" to "some text <br><br>".
     */
    public void processParagraphs(Elements paragraphs) {
        if (!ObjectUtils.isBlank(paragraphs)) {
            for (Element p : paragraphs) {
                if ((p.hasText() || !p.children().isEmpty())) {
                    p.appendChild(new Element(Tag.valueOf("br"), ""));
                    p.appendChild(new Element(Tag.valueOf("br"), ""));
                    p.unwrap();
                } else {
                    p.remove();
                }
            }
        }
    }

    /**
     * Process {@code <span>} tags.
     *
     * By default, unwraps them.
     */
    public void processSpans(Elements spans) {
        if (!ObjectUtils.isBlank(spans)) {
            spans.unwrap();
        }
    }

    /**
     * Process {@code <strong>} tags.
     *
     * By default, replaces them with {@code <b>} tags.
     */
    public void processStrong(Elements strongElements) {
        if (!ObjectUtils.isBlank(strongElements)) {
            strongElements.tagName("b");
        }
    }
}
