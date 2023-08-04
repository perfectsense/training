package brightspot.importtransformers.element;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import brightspot.image.WebImage;
import brightspot.importapi.ImportObjectModification;
import brightspot.importapi.ImportTransformer;
import brightspot.importapi.ImportingDatabase;
import brightspot.link.InternalLink;
import brightspot.link.Linkable;
import brightspot.rte.blockquote.BlockQuoteRichTextElement;
import brightspot.rte.iframe.IframeEmbedRichTextElement;
import brightspot.rte.image.ImageRichTextElement;
import brightspot.rte.link.LinkRichTextElement;
import brightspot.rte.video.VideoRichTextElement;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility to convert generic html into a format suitable for the Brightspot Rich Text editor.
 *
 * Each tag type is processed by a separate method so the conversion can be customized.
 */
public class ImportRichTextImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportRichTextImporter.class);

    private static final String DATA_ATTR = "data-state";
    private static final String LINK_RTE_TAG_NAME = "a";

    private static final String EXTERNAL_ID_ATTRIBUTE = "externalid";

    private static final List<String> BSP_EMBEDS = Arrays.asList(
        BlockQuoteRichTextElement.TAG_NAME,
        ImportElementUtil.EXTERNAL_CONTENT_RTE_TAG,
        IframeEmbedRichTextElement.TAG_NAME,
        ImageRichTextElement.TAG_NAME,
        VideoRichTextElement.TAG_NAME
    );

    private static final List<String> STYLING_TAGS = Arrays.asList(
        "i", "b", "h1", "h2", "h3", "h4", "h5", "h6"
    );

    private final Safelist safelist;

    private ImportTransformer<?> transformer;

    private Site site;

    private boolean createBspBlockEmbeds = true;

    /**
     * Construct a RteImporter which will not perform any cleaning on input html.
     */
    public ImportRichTextImporter() {
        this(null, null);
    }

    /**
     * Construct a RteImporter which will use the given whitelist to clean input html.
     *
     * @param safelist a {@link Safelist} which will be used to clean the raw html before parsing it; may be null, in
     * which case no cleaning will be performed
     */
    public ImportRichTextImporter(ImportTransformer<?> transformer, Safelist safelist) {
        this.transformer = transformer;
        this.safelist = safelist;
        this.site = ((ImportingDatabase) Database.Static.getDefault()).getSite();
    }

    public ImportRichTextImporter createBspBlockEmbeds(boolean createBspBlockEmbeds) {
        this.createBspBlockEmbeds = createBspBlockEmbeds;
        return this;
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

        html = html.replace("\r\n", "<br>");
        html = html.replace("\t", "");

        if (createBspBlockEmbeds) {
            html = convertExternalUrls(html);
        }

        Element docBody = parseHtml(html, sourceUrl);
        processElements(docBody);
        return docBody.html();
    }

    private static String convertExternalUrls(String html) {
        StringBuilder builder = new StringBuilder();

        for (String text : html.split("<br>")) {

            if (StringUtils.isNotBlank(text) && !text.contains("<") && !text.contains(" ")
                && ImportElementUtil.isSupportedExternalContent(text)) {
                text = Optional.of(ImportElementUtil.createExternalContentElement(text))
                    .map(Element::outerHtml)
                    .orElse(text);
            }

            builder.append(text).append("<br>");
        }
        return builder.toString();
    }

    /**
     * Parse the given html into a DOM tree using Jsoup, using {@code whitelist} to clean it.
     *
     * @param html the raw html to convert
     * @param sourceUrl the url the html was obtained from (used for converting relative hrefs to absolute); may be
     * null
     * @return an {@link Element} containing the root of the html document, cleaned and ready to be processed
     */
    private Element parseHtml(String html, String sourceUrl) {
        Document doc = Jsoup.parseBodyFragment(html, sourceUrl != null ? sourceUrl : "");

        if (safelist != null) {
            doc = new Cleaner(safelist).clean(doc);
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
    private void processElements(Element docBody) {

        // inline
        processEm(docBody.getElementsByTag("em"));
        processStrong(docBody.getElementsByTag("strong"));

        cleanList(docBody.getElementsByTag("ul"));
        cleanList(docBody.getElementsByTag("ol"));

        if (createBspBlockEmbeds) {
            processImg(docBody.getElementsByTag("img"));
        }

        processSpans(docBody.getElementsByTag("span"));
        processInternalLinks(docBody.getElementsByTag("a"));

        // block
        if (createBspBlockEmbeds) {
            processBlockQuotes(docBody.getElementsByTag("blockquote"));
            processIframeElements(docBody.getElementsByTag("iframe"));
            processVideoElements(docBody.getElementsByTag("video"));
        }

        processDivs(docBody.getElementsByTag("div"));
        processParagraphs(docBody.getElementsByTag("p"));

        if (createBspBlockEmbeds) {
            for (String styleTag : STYLING_TAGS) {
                unwrapBspElements(docBody.getElementsByTag(styleTag));
            }
        }
    }

    /**
     * Process {@code <blockquote>} tags.
     *
     * By default, converts them to {@link brightspot.rte.blockquote.BlockQuoteRichTextElement}, stripping their
     * internal html, if any.
     */
    private void processBlockQuotes(Elements quoteElements) {
        if (ObjectUtils.isBlank(quoteElements)) {
            return;
        }

        for (Element quoteElement : quoteElements) {

            Element element = quoteElement.select("a").stream()
                .map(a -> a.attr("href"))
                .filter(ImportElementUtil::isSupportedExternalContent)
                .map(ImportElementUtil::createExternalContentElement)
                .findFirst().orElse(null);
            if (element != null) {
                quoteElement.replaceWith(element);
                continue;
            }

            if (quoteElement.hasText() || !quoteElement.children().isEmpty()) {

                BlockQuoteRichTextElement quoteRTE = new BlockQuoteRichTextElement();
                quoteRTE.setText(quoteElement.text());
                if (quoteElement.hasAttr("cite")) {
                    quoteRTE.setAttribution(quoteElement.attr("cite"));
                }

                Element bspQuote = new Element(Tag.valueOf(BlockQuoteRichTextElement.TAG_NAME), "");
                bspQuote.attr(DATA_ATTR, quoteRTE.toAttributes().get(DATA_ATTR));
                bspQuote.text(quoteRTE.toBody());
                quoteElement.replaceWith(bspQuote);
            } else {
                quoteElement.remove();
            }
        }
    }

    /**
     * Process {@code <iframe>} tags.
     *
     * Check if it can be converted to a {@link brightspot.rte.video.VideoRichTextElement} or an
     * {@link com.psddev.cms.rte.ExternalContentRichTextElement} but will convert to a
     * {@link brightspot.rte.iframe.IframeEmbedRichTextElement} as the default.
     */
    private void processIframeElements(Elements iframeElements) {
        if (ObjectUtils.isBlank(iframeElements)) {
            return;
        }

        for (Element iframeElement : iframeElements) {

            String iframeSrc = iframeElement.attr("src");
            if (StringUtils.isBlank(iframeSrc)) {
                continue;
            }

            if (ImportElementUtil.isSupportedVideoPlayer(iframeSrc)) {
                Optional.ofNullable(ImportElementUtil.createVideo(iframeSrc, site))
                    .map(ImportElementUtil::createVideoElement)
                    .ifPresent(iframeElement::replaceWith);

            } else if (ImportElementUtil.isSupportedExternalContent(iframeSrc)) {
                Optional.of(ImportElementUtil.createExternalContentElement(iframeSrc))
                    .ifPresent(iframeElement::replaceWith);

            } else {
                Optional.ofNullable(ImportElementUtil.createIframe(iframeSrc, site,
                        iframeElement.attr("name"), iframeElement.attr("height"), iframeElement.attr("width")))
                    .map(ImportElementUtil::createIframeElement)
                    .ifPresent(iframeElement::replaceWith);
            }
        }
    }

    /**
     * Process {@code <video>} tags.
     *
     * Attempts to convert to a {@link brightspot.rte.video.VideoRichTextElement} Limited handling provided, using
     * non-standard {@code externalid} attribute to retrieve previously imported {@link brightspot.video.Video}. Does
     * not support converting standard {@code <video>} elements into BSP video embeds.
     */
    private void processVideoElements(Elements iframeElements) {
        if (ObjectUtils.isBlank(iframeElements)) {
            return;
        }
        for (Element iframeElement : iframeElements) {

            Optional.of(iframeElement.attr(EXTERNAL_ID_ATTRIBUTE))
                .filter(StringUtils::isNotBlank)
                .map(externalId -> ImportElementUtil.retrieveVideo(externalId, site))
                .map(ImportElementUtil::createVideoElement)
                .ifPresent(iframeElement::replaceWith);
        }
    }

    /**
     * Process {@code <div>} tags.
     *
     * By default, converts "<div>some text </div>" to "some text <br>".
     */
    private void processDivs(Elements divs) {
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
    private void processEm(Elements emElements) {
        if (!ObjectUtils.isBlank(emElements)) {
            emElements.tagName("i");
        }
    }

    /**
     * Process {@code <img>} tags.
     *
     * By default, imports the image using
     * {@link ImportElementUtil#createImage(String, Element, ImportTransformer, Site)} and converts the tag to an
     * {@link brightspot.rte.image.ImageRichTextElement}.
     */
    private void processImg(Elements imgElements) {
        if (ObjectUtils.isBlank(imgElements)) {
            return;
        }

        for (Element imgElement : imgElements) {

            String imageSrc = ObjectUtils.firstNonBlank(imgElement.attr("abs:src"), imgElement.attr("src"));
            if (StringUtils.isBlank(imageSrc)) {
                continue;
            }

            imageSrc = StringUtils.substringBefore(imageSrc, "?");

            String imageExternalId = imgElement.attr("external-id");

            try {

                WebImage image = null;
                if (StringUtils.isNotBlank(imageExternalId)) {
                    image = Query.from(WebImage.class)
                        .where(ImportObjectModification.EXTERNAL_ID_FIELD_NAME + " = " + imageExternalId)
                        .and("cms.site.owner = ?", site)
                        .first();
                }

                if (image == null) {
                    image = ImportElementUtil.createImage(imageSrc, imgElement, transformer, site);
                }

                if (image == null) {
                    continue;
                }

                Element bspImage = ImportElementUtil.createImgElement(image, imgElement.attr("alt"),
                    StringEscapeUtils.unescapeHtml4(imgElement.attr("caption")),
                    StringEscapeUtils.unescapeHtml4(imgElement.attr("credit")));
                if (bspImage == null) {
                    continue;
                }

                imgElement.replaceWith(bspImage);

            } catch (Exception e) {
                LOGGER.error("Failed to convert image {} due to {}", imageSrc, e.getMessage());
            }
        }
    }

    /**
     * Process {@code <a>} tags.
     *
     * Checks if the <a> element references a BSP object, and if so converts it to a
     * {@link brightspot.rte.link.LinkRichTextElement}.
     */
    private void processInternalLinks(Elements linkElements) {
        if (ObjectUtils.isBlank(linkElements)) {
            return;
        }

        for (Element linkElement : linkElements) {
            String href = linkElement.attr("href");
            if (StringUtils.isBlank(href)) {
                continue;
            }

            Linkable linkable = Optional.ofNullable(Directory.Static.findByPath(site, href))
                .filter(Linkable.class::isInstance)
                .map(Linkable.class::cast)
                .orElse(null);
            if (linkable == null) {
                continue;
            }

            InternalLink internalLink = new InternalLink();
            internalLink.setItem(linkable);

            LinkRichTextElement linkRte = new LinkRichTextElement();
            linkRte.setLink(internalLink);
            linkRte.setLinkText(linkElement.text());

            Element bspLink = new Element(Tag.valueOf(LINK_RTE_TAG_NAME), "");
            bspLink.attr(DATA_ATTR, linkRte.toAttributes().get(DATA_ATTR));
            bspLink.text(linkRte.toBody());

            linkElement.replaceWith(bspLink);
        }
    }

    private void cleanList(Elements listElements) {
        if (ObjectUtils.isBlank(listElements)) {
            return;
        }

        for (Element listElement : listElements) {
            for (Node node : listElement.childNodes()) {
                if (node instanceof TextNode && (node.outerHtml().contains("\r\n")
                    || StringUtils.isBlank(((TextNode) node).text()))) {

                    String text = ((TextNode) node).text();
                    if (StringUtils.isBlank(text) || text.contains("\r\n")) {
                        node.remove();
                    }

                } else if (node instanceof Element && ((Element) node).tagName().equals("br")) {
                    node.remove();
                }
            }
        }
    }

    /**
     * Process {@code <p>} tags.
     *
     * By default, converts "<p>some text </p>" to "some text <br><br>".
     */
    private void processParagraphs(Elements paragraphs) {
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
    private void processSpans(Elements spans) {
        if (!ObjectUtils.isBlank(spans)) {
            spans.unwrap();
        }
    }

    /**
     * Process {@code <strong>} tags.
     *
     * By default, replaces them with {@code <b>} tags.
     */
    private void processStrong(Elements strongElements) {
        if (!ObjectUtils.isBlank(strongElements)) {
            strongElements.tagName("b");
        }
    }

    /**
     * Unwrap BSP elements wrapped by styling tags
     *
     * @param elementsToUnwrap styling elements to check
     */
    private void unwrapBspElements(Elements elementsToUnwrap) {
        if (ObjectUtils.isBlank(elementsToUnwrap)) {
            return;
        }

        for (Element elementToUnwrap : elementsToUnwrap) {
            if (Optional.of(elementToUnwrap.children())
                .flatMap(elements -> elements.stream()
                    .filter(element -> BSP_EMBEDS.contains(element.tagName()))
                    .findFirst())
                .isPresent()) {

                elementToUnwrap.unwrap();
            }
        }
    }

}
