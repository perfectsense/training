package brightspot.core.tool;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import brightspot.core.image.ImageOption;
import brightspot.core.image.ImageRichTextElement;
import com.google.common.base.Preconditions;
import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.db.RichTextElementTagSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.rte.EditorialMarkupRichTextPreprocessor;
import com.psddev.cms.rte.RichTextPreprocessor;
import com.psddev.cms.rte.RichTextViewBuilder;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;
import com.psddev.styleguide.core.enhancement.EnhancementView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.NodeTraversor;

public final class RichTextUtils {

    private RichTextUtils() {
    }

    /**
     * @deprecated Use {@link #buildInlineHtml(Database, String, BiFunction)} instead.
     */
    @Deprecated
    public static CharSequence buildInlineHtml(String html, BiFunction<Class<?>, RichTextElement, Object> createView) {
        return buildInlineHtml(Database.Static.getDefault(), html, createView);
    }

    /**
     * Builds a displayable output from the given {@code html} for use in inline context, such as article headline,
     * converting all rich text elements using the given {@code createView} function.
     *
     * @param db The database where rich text elements are stored.
     * @param html Nullable.
     * @param createView Nonnull.
     */
    public static CharSequence buildInlineHtml(
        Database db,
        String html,
        BiFunction<Class<?>, RichTextElement, Object> createView) {
        Preconditions.checkNotNull(createView);

        if (!StringUtils.isBlank(html)) {
            return new RichTextViewBuilder<>(db, html)
                .addPreprocessor(new EditorialMarkupRichTextPreprocessor())
                .elementToView(e -> createView.apply(EnhancementView.class, e))
                .buildHtml();
        }

        return null;
    }

    /**
     * @deprecated Use {@link #buildHtml(Database, String, Function)} instead.
     */
    @Deprecated
    public static <V> List<V> buildHtml(String html, Function<RichTextElement, V> createView) {
        return buildHtml(Database.Static.getDefault(), html, createView);
    }

    /**
     * Builds a displayable output from the given {@code html} for use in block context, such as article body,
     * converting all rich text elements using the given {@code createView} function.
     *
     * @param db The database where the rich text element will be stored.
     * @param html Nullable.
     * @param createView Nonnull.
     */
    public static <V> List<V> buildHtml(Database db, String html, Function<RichTextElement, V> createView) {
        Preconditions.checkNotNull(createView);

        if (!StringUtils.isBlank(html)) {
            return RichTextViewBuilder.build(db, html, createView);
        }

        return null;
    }

    /**
     * Returns true if the rich text string is blank, false otherwise.
     *
     * @param richText Nullable.
     * @return true if blank.
     */
    public static boolean isBlank(String richText) {
        return richText == null || StringUtils.isBlank((richText).replaceAll("<br\\s*/?>", ""));
    }

    /**
     * Preprocesses rich text.
     *
     * @param richText the rich text to process.
     * @param preprocessors the list of preprocessors to use.
     * @return the preprocessed rich text.
     */
    public static String preprocessRichText(String richText, RichTextPreprocessor... preprocessors) {

        if (preprocessors == null) {
            return richText;
        }

        Document document = documentFromRichText(richText);

        Element body = document.body();

        for (RichTextPreprocessor preprocessor : preprocessors) {
            preprocessor.preprocess(body);
        }

        richText = document.body().html();

        return richText;
    }

    /**
     * Creates a properly initialized Jsoup Document from the given rich text.
     *
     * @param richText the rich text to parse.
     * @return a Jsoup Document.
     */
    public static Document documentFromRichText(String richText) {

        Document document = Jsoup.parseBodyFragment(richText);
        document.outputSettings().prettyPrint(false);

        return document;
    }

    /**
     * Converts the provided rich text {@link String} into an equivalent plain text {@link String}.
     *
     * @param richText a rich text {@link String} (nullable).
     * @return a plain text {@link String} (nullable).
     */
    public static String richTextToPlainText(String richText) {

        // helper methods
        if (ObjectUtils.isBlank(richText)) {
            return richText;
        }

        // Parse str into a Document
        Document doc = documentFromRichText(richText);

        // Remove / unwrap track changes tags
        new EditorialMarkupRichTextPreprocessor().preprocess(doc.body());

        // Clean the document.
        doc = new Cleaner(Whitelist.none()).clean(doc);

        // Get back the string of the body.
        return doc.body().text();
    }

    /**
     * Strip all elements defined as {@link RichTextElement}s.
     *
     * @param richText the rich text to parse.
     * @return the rich text without block-level {@link RichTextElement}s.
     */
    public static String stripRichTextElements(String richText) {
        // helper methods
        if (ObjectUtils.isBlank(richText)) {
            return richText;
        }

        // Parse str into a Document
        Document doc = documentFromRichText(richText);

        // Remove / unwrap track changes tags
        new EditorialMarkupRichTextPreprocessor().preprocess(doc.body());

        Map<String, ObjectType> concreteTagTypes = RichTextElement.getConcreteTagTypes();

        // Find all defined RichTextElements as a comma delimited list
        String cssQuery = concreteTagTypes
            .keySet()
            .stream()
            .collect(Collectors.joining(","));

        // Completely remove all tags
        for (Element element : doc.select(cssQuery)) {
            ObjectType type = concreteTagTypes.get(element.tagName());

            boolean isBlock = Optional.ofNullable(type.as(ToolUi.class).getRichTextElementTagSettings())
                .map(RichTextElementTagSettings::isBlock)
                .orElse(false);

            if (isBlock) {
                element.remove();
            } else {
                element.unwrap();
            }
        }

        // Get back the string of the body.
        return doc.body().html();
    }

    /**
     * @deprecated Use {@link RichTextElement#fromElement} instead.
     */
    @Deprecated
    public static RichTextElement richTextElementFromElement(Element element) {
        Preconditions.checkNotNull(element);

        ObjectType tagType = RichTextElement.getConcreteTagTypes().get(element.tagName());

        if (tagType == null) {
            return null;
        }

        RichTextElement rte = (RichTextElement) tagType.createObject(null);

        rte.fromAttributes(StreamSupport
            .stream(element.attributes().spliterator(), false)
            .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue)));

        rte.fromBody(element.html());

        return rte;
    }

    /**
     * Traverses a RichText field, allowing all of the specified {@link RichTextElementObserver} an opportunity to
     * observe each {@link RichTextElement}.
     *
     * @param richText The rich text String.
     * @param observers A list of observers to observe each RichTextElement.
     */
    public static void observeRichTextElements(String richText, List<RichTextElementObserver> observers) {

        if (richText == null || observers == null || observers.isEmpty()) {
            return;
        }

        NodeTraversor.traverse(new RichTextElementObserverVisitor(observers), documentFromRichText(richText));
    }

    /**
     * Returns the first paragraph of the rich text.
     * <p/>
     * <strong>Note:</strong> strips all {@link RichTextElement}s prior to determining the firs paragraph contents.
     *
     * @param richText a rich text {@link String} (nullable).
     * @return a plain text {@link String} (nullable).
     */
    public static String getFirstBodyParagraph(String richText) {

        if (richText != null) {

            richText = stripRichTextElements(richText);

            String[] parts = richText.split("<br\\s?/?>");

            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                String firstParagraph = RichTextUtils.richTextToPlainText(part);
                if (!ObjectUtils.isBlank(firstParagraph)) {
                    return firstParagraph;
                }
            }
        }

        return null;
    }

    /**
     * Returns all images within the provided rich text.
     *
     * @param richText a rich text {@link String} (nullable).
     * @return a {@link List} of {@link ImageOption ImageOptions} (never null).
     */
    public static List<ImageOption> getImagesFromRichText(String richText) {

        if (!ObjectUtils.isBlank(richText)) {
            Document bodyDocument = RichTextUtils.documentFromRichText(richText);
            return bodyDocument.select(ImageRichTextElement.TAG_NAME)
                .stream()
                .map(e -> {
                    ImageRichTextElement iRte = new ImageRichTextElement();
                    iRte.fromAttributes(XmlUtils.getAttributeMap(e));
                    iRte.fromBody(e.html());
                    return iRte.getImage();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * Returns the first image within the provided rich text.
     *
     * @param richText a rich text {@link String} (nullable).
     * @return an {@link ImageOption} or {@code null}, if no image was found.
     */
    public static ImageOption getFirstImageFromRichText(String richText) {

        if (!ObjectUtils.isBlank(richText)) {

            return RichTextUtils.documentFromRichText(richText).select(ImageRichTextElement.TAG_NAME)
                .stream()
                .map(e -> {
                    ImageRichTextElement imageRTE = new ImageRichTextElement();
                    imageRTE.fromAttributes(XmlUtils.getAttributeMap(e));
                    imageRTE.fromBody(e.html());
                    return imageRTE.getImage();
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        }

        return null;
    }
}
