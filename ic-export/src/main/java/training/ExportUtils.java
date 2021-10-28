package training;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import brightspot.core.article.Article;
import brightspot.core.carousel.CarouselRichTextElement;
import brightspot.core.carousel.ExistingGalleryItemStream;
import brightspot.core.gallery.Gallery;
import brightspot.core.image.Image;
import brightspot.core.image.ImageRichTextElement;
import brightspot.core.image.OneOffImageOption;
import brightspot.core.imageitemstream.AdvancedImageItemStream;
import brightspot.core.imageitemstream.ImageItemPromo;
import brightspot.core.link.Attribute;
import brightspot.core.link.ExternalLink;
import brightspot.core.link.InternalLink;
import brightspot.core.link.LinkRichTextElement;
import brightspot.core.person.Author;
import brightspot.core.quote.QuoteRichTextElement;
import brightspot.core.rte.heading.HeadingThree;
import brightspot.core.rte.heading.HeadingTwo;
import brightspot.core.section.Section;
import brightspot.core.tag.Tag;
import brightspot.core.video.VideoLead;
import brightspot.core.video.VideoRichTextElement;
import brightspot.google.youtube.YouTubeUrlVideoProvider;
import brightspot.vimeo.VimeoUrlVideoProvider;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUser;
import com.psddev.cms.rte.EditorialMarkupRichTextPreprocessor;
import com.psddev.cms.rte.ExternalContentRichTextElement;
import com.psddev.cms.view.DelegateView;
import com.psddev.cms.view.MapViewRenderer;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.ViewTemplateLoader;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.UuidUtils;
import com.psddev.dari.web.WebRequest;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import training.rte.RichTextConverter;

public class ExportUtils {

    static final UUID IC_SITE_ID = UUID.fromString("0000015d-6154-d664-a95d-e3ff95640000");

    private static final Map<Class<?>, String> TYPE_MAPPINGS;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportUtils.class);

    static {
        ImmutableMap.Builder<Class<?>, String> builder = ImmutableMap.builder();
        builder
            .put(AdvancedImageItemStream.class, "brightspot.imageitemstream.AdvancedImageItemStream")
            .put(Attribute.class, "brightspot.link.Attribute")
            .put(Author.class, "brightspot.author.PersonAuthor")
            .put(Article.class, "brightspot.article.Article")
            .put(CarouselRichTextElement.class, "brightspot.rte.carousel.CarouselRichTextElement")
            .put(DefaultModulePageLeadViewModel.class, "brightspot.page.PageHeading")
            .put(ExistingGalleryItemStream.class, "brightspot.imageitemstream.ExistingImageItemStream")
            .put(ExternalContentRichTextElement.class, "com.psddev.cms.rte.ExternalContentRichTextElement")
            .put(ExternalLink.class, "brightspot.link.ExternalLink")
            .put(Gallery.class, "brightspot.gallery.Gallery")
            .put(HeadingThree.class, "brightspot.rte.heading.HeadingThree")
            .put(HeadingTwo.class, "brightspot.rte.heading.HeadingTwo")
            .put(Image.class, "brightspot.image.WebImage")
            .put(ImageItemPromo.class, "brightspot.imageitemstream.WebImageItem")
            .put(ImageRichTextElement.class, "brightspot.rte.image.ImageRichTextElement")
            .put(InternalLink.class, "brightspot.link.InternalLink")
            .put(LinkRichTextElement.class, "brightspot.rte.link.LinkRichTextElement")
            .put(OneOffImageOption.class, "brightspot.image.WebImage")
            .put(OneOffImagePageHeadingViewModel.class, "brightspot.page.PageHeading")
            .put(QuoteRichTextElement.class, "brightspot.rte.blockquote.BlockQuoteRichTextElement")
            .put(Section.class, "brightspot.section.SectionPage")
            .put(OneOffImageLeadViewModel.class, "brightspot.image.ImageLead")
            .put(Site.class, "com.psddev.cms.db.Site")
            .put(Tag.class, "brightspot.tag.TagPage")
            .put(ToolUser.class, "com.psddev.cms.db.ToolUser")
            .put(VideoLead.class, "brightspot.video.VideoLead")
            .put(VideoRichTextElement.class, "brightspot.rte.video.VideoRichTextElement")
            .put(VimeoUrlVideoProvider.class, "brightspot.vimeo.VimeoUrlVideo")
            .put(YouTubeUrlVideoProvider.class, "brightspot.google.youtube.YouTubeUrlVideo")
        ;

        TYPE_MAPPINGS = builder.build();
    }

    public static Map<String, String> buildRef(Recordable reference) {
        return buildRef(reference, reference);
    }

    public static Map<String, String> buildRef(Recordable ref, Recordable typeSource) {
        WebRequest.getCurrent().as(ExportRefsWebExtension.class).addRef(ref);
        return ImmutableMap.of(
            "_ref", ref.getState().getId().toString(),
            "_type", getExportType(typeSource));
    }

    public static String getExportType(Recordable recordable) {
        return Optional.ofNullable(TYPE_MAPPINGS.get(recordable.getClass()))
            .map(name -> UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8)))
            .map(UUID::toString)
            .orElseThrow(() -> new IllegalArgumentException(
                "Unhandled export type: " + recordable.getClass().getName()));
    }

    public static String getExportType(ViewModel<?> viewModel) {
        return Optional.ofNullable(TYPE_MAPPINGS.get(viewModel.getClass()))
            .map(name -> UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8)))
            .map(UUID::toString)
            .orElseThrow(() -> new IllegalArgumentException(
                "Unhandled export type for viewmodel: " + viewModel.getClass().getName()));
    }

    public static Map<String, Object> export(Recordable recordable, ViewTemplateLoader loader) {
        try {
            WebRequest.getCurrent().as(ExportRefsWebExtension.class).addRef(recordable);

            Class<? extends ViewModel<? super Recordable>> viewModelClass = ViewModel.findViewModelClass(
                ExportEntryView.class,
                recordable);
            if (viewModelClass == null) {
                throw new IllegalArgumentException("ExportEntryView not found for " + recordable.getClass().getName());
            }

            ViewModel<? super Recordable> viewModel = new ViewModel.DefaultCreator().createViewModel(
                viewModelClass,
                recordable,
                new ViewResponse());
            if (viewModel == null) {
                return null;
            }

            Object result;
            if (viewModel instanceof DelegateView<?>) {
                result = ((DelegateView<?>) viewModel).getDelegate();
            } else {
                result = viewModel;
            }

            return new MapViewRenderer().getMap(result, loader);

        } catch (RuntimeException e) {
            LOGGER.error(
                "Failed to export " + recordable.getClass().getSimpleName() + " " + recordable.getState().getId(),
                e);
            return null;
        }
    }

    public static <T extends Recordable> CharSequence processRichText(
        T parent,
        Function<T, String> richTextGetter) {

        Objects.requireNonNull(parent);
        Objects.requireNonNull(richTextGetter);

        String richText = richTextGetter.apply(parent);
        if (StringUtils.isBlank(richText)) {
            return null;
        }

        List<Element> elements = new ArrayList<>();

        Document document = Jsoup.parseBodyFragment(richText);
        document.outputSettings().prettyPrint(false);

        new EditorialMarkupRichTextPreprocessor().preprocess(document.body());

        NodeTraversor.traverse(new NodeVisitor() {

            @Override
            public void head(Node node, int depth) {

            }

            @Override
            public void tail(Node node, int depth) {
                Optional.ofNullable(node)
                    .filter(Element.class::isInstance)
                    .map(Element.class::cast)
                    .filter(e -> RichTextElement.fromElement(parent, e) != null)
                    .ifPresent(elements::add);
            }
        }, document.body());

        elements.forEach(e -> RichTextConverter.getConverter(e.tagName()).convert(parent, e));

        document.body().childNodes();

        return document.body().html();
    }

    public static Image saveOneOffImage(OneOffImageOption imageOption) {
        Site icSite = Objects.requireNonNull(Query.from(Site.class)
            .where("name = ?", "Inspire Confidence")
            .first());

        Date publishDate = new Date(UuidUtils.sequentialUuidTimestamp(imageOption.getId()));

        Image image = new Image(); // TODO prevent duplicates?
        image.setFile(imageOption.getFile());
        image.setAltText(imageOption.getAltText());
        image.as(Content.ObjectModification.class).setPublishDate(publishDate);
        image.as(Content.ObjectModification.class).setUpdateDate(publishDate);
        image.as(Site.ObjectModification.class).setOwner(icSite);

        // regular save triggers Rekognition
        image.getState().saveUnsafely();

        return image;
    }
}
