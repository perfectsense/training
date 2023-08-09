package brightspot.importtransformers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.article.ArticleLead;
import brightspot.author.Author;
import brightspot.image.ImageLead;
import brightspot.image.ImageLeadSubstitution;
import brightspot.image.WebImage;
import brightspot.image.WebImageValidMimeTypesProvider;
import brightspot.imageitemstream.AdvancedImageItemStream;
import brightspot.imageitemstream.ImageItemStream;
import brightspot.imageitemstream.WebImageItem;
import brightspot.importapi.ImportTransformer;
import brightspot.page.ModulePageLead;
import brightspot.page.PageHeading;
import brightspot.page.PageHeadingSubstitution;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.section.Section;
import brightspot.section.SectionPage;
import brightspot.tag.Tag;
import brightspot.video.Video;
import brightspot.video.VideoLead;
import brightspot.video.VideoLeadSubstitution;
import com.psddev.dari.db.StringException;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.TypeDefinition;
import org.apache.commons.lang3.StringUtils;

public final class ImportTransformerUtil {

    private ImportTransformerUtil() {
    }

    public static StorageItem validateImageFile(StorageItem file, String fileUrl) throws StringException {
        if (file == null) {
            throw new StringException(String.format("No file retrieved for WebImage URL: %s", fileUrl));
        }
        if (!TypeDefinition.getInstance(WebImageValidMimeTypesProvider.class)
            .newInstance()
            .get()
            .contains(file.getContentType())) {
            throw new StringException(String.format(
                "Invalid mime type for WebImage URL: %s / Mime-Type: %s",
                fileUrl,
                file.getContentType()));
        }
        return file;
    }

    // ------ ARTICLE LEAD SUPPORT ------ //

    static ArticleLead retrieveLead(
        VideoImportTransformer videoReference,
        WebImageImportTransformer imageReference,
        ImportTransformer<?> mainTransformer) {
        ArticleLead lead = retrieveVideoLead(videoReference, mainTransformer);
        if (lead == null) {
            lead = retrieveImageLead(imageReference, mainTransformer);
        }
        return lead;
    }

    static ImageLeadSubstitution retrieveImageLead(
        WebImageImportTransformer imageReference,
        ImportTransformer<?> mainTransformer) {
        if (imageReference == null) {
            return null;
        }
        WebImage image = imageReference.findOrCreate(mainTransformer);
        if (image == null) {
            return null;
        }
        ImageLead imageLead = new ImageLead();
        imageLead.setImage(image);
        return imageLead.as(ImageLeadSubstitution.class);
    }

    static VideoLeadSubstitution retrieveVideoLead(
        VideoImportTransformer videoReference,
        ImportTransformer<?> mainTransformer) {
        if (videoReference == null) {
            return null;
        }
        Video video = videoReference.findOrCreate(mainTransformer);
        if (video == null) {
            return null;
        }
        VideoLead videoLead = new VideoLead();
        videoLead.setVideo(video);
        return videoLead.as(VideoLeadSubstitution.class);
    }

    // ------ MODULE LEAD SUPPORT ------ //

    static ModulePageLead retrieveModuleLead(
        WebImageImportTransformer imageReference,
        ImportTransformer<?> mainTransformer) {
        if (imageReference == null) {
            return null;
        }
        WebImage image = imageReference.findOrCreate(mainTransformer);
        if (image == null) {
            return null;
        }
        PageHeading lead = new PageHeading();
        lead.setBackgroundImage(image);
        return lead.as(PageHeadingSubstitution.class);
    }

    // ------ IMAGE SUPPORT ------ //

    static void setPromoImage(
        PagePromotableWithOverrides promotable,
        WebImageImportTransformer imageReference,
        ImportTransformer<?> mainTransformer) {
        Optional.ofNullable(imageReference)
            .map(ref -> ref.findOrCreate(mainTransformer))
            .ifPresent(promoImage -> promotable.asPagePromotableWithOverridesData().setPromoImage(promoImage));
    }

    static ImageItemStream createSlideItemStream(
        List<WebImageImportTransformer> imageReferences,
        ImportTransformer<?> mainTransformer) {
        if (ObjectUtils.isBlank(imageReferences)) {
            return null;
        }

        List<WebImageItem> slides = imageReferences.stream()
            .map(ref -> {
                WebImage image = ref.findOrCreate(mainTransformer);
                if (image == null) {
                    return null;
                }
                WebImageItem slide = new WebImageItem();
                slide.setItem(image);
                slide.setTitle(ref.getCaption());
                return slide;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if (slides.isEmpty()) {
            return null;
        }

        AdvancedImageItemStream itemStream = new AdvancedImageItemStream();
        itemStream.getItems().addAll(slides);
        return itemStream;
    }

    // ------ SECTION SUPPORT ------ //

    static SectionPage retrieveSectionPage(
        SectionPageImportTransformer sectionReference,
        ImportTransformer<?> mainTransformer) {
        return Optional.ofNullable(sectionReference)
            .map(s -> s.findOrCreate(mainTransformer))
            .orElse(null);
    }

    static Set<Section> retrieveSecondarySections(
        List<SectionPageImportTransformer> sectionReferences,
        ImportTransformer<?> mainTransformer) {
        if (ObjectUtils.isBlank(sectionReferences)) {
            return null;
        }
        Set<Section> secondarySections = new HashSet<>();
        for (SectionPageImportTransformer sectionReference : sectionReferences) {
            Optional.ofNullable(sectionReference.findOrCreate(mainTransformer))
                .map(Section.class::cast)
                .ifPresent(secondarySections::add);
        }
        return secondarySections;
    }

    // ------ AUTHOR SUPPORT ------ //

    static List<Author> retrieveAuthors(
        List<PersonAuthorImportTransformer> authorReferences,
        ImportTransformer<?> mainTransformer) {
        if (ObjectUtils.isBlank(authorReferences)) {
            return null;
        }
        List<Author> authorList = new ArrayList<>();
        for (PersonAuthorImportTransformer authorReference : authorReferences) {
            Optional.ofNullable(authorReference.findOrCreate(mainTransformer))
                .ifPresent(authorList::add);
        }
        return authorList;
    }

    // ------ TAG SUPPORT ------ //

    static List<Tag> retrieveTags(List<TagPageImportTransformer> tagReferences, ImportTransformer<?> mainTransformer) {
        if (ObjectUtils.isBlank(tagReferences)) {
            return null;
        }
        List<Tag> tagList = new ArrayList<>();
        for (TagPageImportTransformer tagReference : tagReferences) {
            Optional.ofNullable(tagReference.findOrCreate(mainTransformer))
                .ifPresent(tagList::add);
        }
        return tagList;
    }

    public static String prependFileBaseUrlIfNeeded(String url, String fileBaseUrl) {
        if (StringUtils.isBlank(url) || StringUtils.isBlank(fileBaseUrl)) {
            return url;
        }
        if (!StringUtils.startsWith(url, "http")) {
            return StringUtils.prependIfMissing(url, fileBaseUrl);
        }
        return url;
    }
}
