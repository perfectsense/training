package brightspot.importtransformers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import brightspot.gallery.Gallery;
import brightspot.imageitemstream.ImageItemStream;
import brightspot.importapi.ImportTransformer;
import brightspot.importapi.element.ImportElement;
import brightspot.importtransformers.element.ImportElementUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.psddev.cms.db.Seo;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class GalleryImportTransformer extends ImportTransformer<Gallery> {

    private static final String HEADLINE_FIELD = "headline";
    private static final String ITEMS_FIELD = "items";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String BODY_FIELD = "body";
    private static final String SECTION_FIELD = "section";
    private static final String AUTHORS_FIELD = "authors";
    private static final String TAGS_FIELD = "tags";

    private static final String PROMO_IMAGE_FIELD = "promoImage";
    private static final String PROMO_DESCRIPTION_FIELD = "promoDescription";
    private static final String SEO_TITLE_FIELD = "seoTitle";
    private static final String SEO_DESCRIPTION_FIELD = "seoDescription";
    private static final String SEO_KEYWORDS_FIELD = "seoKeywords";

    @JsonProperty(HEADLINE_FIELD)
    private String headline;

    @JsonProperty(ITEMS_FIELD)
    private List<WebImageImportTransformer> imageReferences;

    @JsonProperty(DESCRIPTION_FIELD)
    private String description;

    @JsonProperty(BODY_FIELD)
    private List<ImportElement> bodyElements;

    @JsonProperty(PROMO_IMAGE_FIELD)
    private WebImageImportTransformer promoImageReference;

    @JsonProperty(PROMO_DESCRIPTION_FIELD)
    private String promoDescription;

    @JsonProperty(SECTION_FIELD)
    private SectionPageImportTransformer sectionReference;

    @JsonProperty(AUTHORS_FIELD)
    private List<PersonAuthorImportTransformer> authorReferences;

    @JsonProperty(TAGS_FIELD)
    private List<TagPageImportTransformer> tagReferences;

    @JsonProperty(SEO_TITLE_FIELD)
    private String seoTitle;

    @JsonProperty(SEO_DESCRIPTION_FIELD)
    private String seoDescription;

    @JsonProperty(SEO_KEYWORDS_FIELD)
    private Set<String> seoKeywords;

    @Override
    public String getExternalId() {
        return ObjectUtils.firstNonBlank(this.externalId, this.getIdAsString(), this.headline);
    }

    @Override
    public Gallery transform() throws Exception {

        Preconditions.checkArgument(
            StringUtils.isNotBlank(this.getHeadline()),
            "headline not provided for Gallery with externalId [" + this.getExternalId() + "]");
        Preconditions.checkArgument(
            !ObjectUtils.isBlank(this.getImageReferences()),
            "items not provided for Gallery with externalId [" + this.getExternalId() + "]");

        Gallery gallery = (Gallery) this.createNewObject();

        gallery.setTitle(ImportElementUtil.processInlineRichText(this.getHeadline(), this));

        ImageItemStream itemStream = ImportTransformerUtil.createSlideItemStream(this.getImageReferences(), this);
        if (itemStream != null) {
            gallery.setItemStream(itemStream);
        }

        gallery.setDescription(ImportElementUtil.processInlineRichText(this.getDescription(), this));

        gallery.setBody(ImportElementUtil.createBody(this.getBodyElements(), this));

        gallery.asHasSectionWithFieldData()
            .setSection(ImportTransformerUtil.retrieveSectionPage(this.getSectionReference(), this));

        gallery.asHasAuthorsWithFieldData()
            .setAuthors(ImportTransformerUtil.retrieveAuthors(this.getAuthorReferences(), this));

        gallery.asHasTagsWithFieldData().setTags(ImportTransformerUtil.retrieveTags(this.getTagReferences(), this));

        ImportTransformerUtil.setPromoImage(gallery, this.getPromoImageReference(), this);
        gallery.asPagePromotableWithOverridesData()
            .setPromoDescription(ImportElementUtil.processInlineRichText(this.getPromoDescription(), this));

        gallery.asSeoWithFieldsData().setTitle(this.getSeoTitle());
        gallery.asSeoWithFieldsData().setDescription(this.getSeoDescription());
        gallery.as(Seo.ObjectModification.class).setKeywords(this.getSeoKeywords());

        return gallery;
    }

    @Override
    public Predicate getUniqueFieldPredicate() {
        return null;
    }

    public String getHeadline() {
        return headline;
    }

    public List<WebImageImportTransformer> getImageReferences() {
        if (imageReferences == null) {
            imageReferences = new ArrayList<>();
        }
        return imageReferences;
    }

    public String getDescription() {
        return description;
    }

    public List<ImportElement> getBodyElements() {
        if (bodyElements == null) {
            bodyElements = new ArrayList<>();
        }
        return bodyElements;
    }

    public SectionPageImportTransformer getSectionReference() {
        return sectionReference;
    }

    public List<PersonAuthorImportTransformer> getAuthorReferences() {
        if (authorReferences == null) {
            authorReferences = new ArrayList<>();
        }
        return authorReferences;
    }

    public List<TagPageImportTransformer> getTagReferences() {
        if (tagReferences == null) {
            tagReferences = new ArrayList<>();
        }
        return tagReferences;
    }

    public WebImageImportTransformer getPromoImageReference() {
        return promoImageReference;
    }

    public String getPromoDescription() {
        return promoDescription;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public Set<String> getSeoKeywords() {
        if (seoKeywords == null) {
            seoKeywords = new HashSet<>();
        }
        return seoKeywords;
    }
}
