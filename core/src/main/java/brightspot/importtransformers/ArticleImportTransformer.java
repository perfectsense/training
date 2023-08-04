package brightspot.importtransformers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.article.Article;
import brightspot.importapi.ImportTransformer;
import brightspot.importapi.element.ImportElement;
import brightspot.importtransformers.element.ImportElementUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.psddev.cms.db.Seo;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class ArticleImportTransformer extends ImportTransformer<Article> {

    private static final String HEADLINE_FIELD = "headline";
    private static final String SUB_HEADLINE_FIELD = "subheadline";
    private static final String LEAD_IMAGE_FIELD = "leadImage";
    private static final String LEAD_VIDEO_FIELD = "leadVideo";
    private static final String BODY_FIELD = "body";
    private static final String SECTION_FIELD = "section";
    private static final String SECONDARY_SECTIONS_FIELD = "secondarySections";
    private static final String AUTHORS_FIELD = "authors";
    private static final String TAGS_FIELD = "tags";

    private static final String PROMO_IMAGE_FIELD = "promoImage";
    private static final String PROMO_DESCRIPTION_FIELD = "promoDescription";
    private static final String SEO_TITLE_FIELD = "seoTitle";
    private static final String SEO_DESCRIPTION_FIELD = "seoDescription";
    private static final String SEO_KEYWORDS_FIELD = "seoKeywords";

    @JsonProperty(HEADLINE_FIELD)
    private String headline;

    @JsonProperty(SUB_HEADLINE_FIELD)
    private String subheadline;

    @JsonProperty(LEAD_IMAGE_FIELD)
    private WebImageImportTransformer imageReference;

    @JsonProperty(LEAD_VIDEO_FIELD)
    private VideoImportTransformer videoReference;

    @JsonProperty(BODY_FIELD)
    private List<ImportElement> bodyElements;

    @JsonProperty(SECTION_FIELD)
    private SectionPageImportTransformer sectionReference;

    @JsonProperty(SECONDARY_SECTIONS_FIELD)
    private List<SectionPageImportTransformer> secondarySectionReferences;

    @JsonProperty(AUTHORS_FIELD)
    private List<PersonAuthorImportTransformer> authorReferences;

    @JsonProperty(TAGS_FIELD)
    private List<TagPageImportTransformer> tagReferences;

    @JsonProperty(PROMO_IMAGE_FIELD)
    private WebImageImportTransformer promoImageReference;

    @JsonProperty(PROMO_DESCRIPTION_FIELD)
    private String promoDescription;

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
    public Article transform() throws Exception {

        Preconditions.checkArgument(
            StringUtils.isNotBlank(this.getHeadline()),
            "headline not provided for Article with externalId [" + this.getExternalId() + "]");

        Article article = (Article) this.createNewObject();

        article.setHeadline(ImportElementUtil.processInlineRichText(this.getHeadline(), this));
        article.setSubheadline(ImportElementUtil.processInlineRichText(this.getSubheadline(), this));

        Optional.ofNullable(ImportTransformerUtil.retrieveLead(
                this.getVideoReference(),
                this.getImageReference(),
                this))
            .ifPresent(article::setLead);

        article.setBody(ImportElementUtil.createBody(this.getBodyElements(), this));

        article.asHasSectionWithFieldData()
            .setSection(ImportTransformerUtil.retrieveSectionPage(this.getSectionReference(), this));
        article.asHasSecondarySectionsWithFieldData()
            .setSecondarySections(ImportTransformerUtil.retrieveSecondarySections(
                this.getSecondarySectionReferences(),
                this));

        article.asHasAuthorsWithFieldData()
            .setAuthors(ImportTransformerUtil.retrieveAuthors(this.getAuthorReferences(), this));

        article.asHasTagsWithFieldData().setTags(ImportTransformerUtil.retrieveTags(this.getTagReferences(), this));

        ImportTransformerUtil.setPromoImage(article, this.getPromoImageReference(), this);
        article.asPagePromotableWithOverridesData()
            .setPromoDescription(ImportElementUtil.processInlineRichText(this.getPromoDescription(), this));

        article.asSeoWithFieldsData().setTitle(this.getSeoTitle());
        article.asSeoWithFieldsData().setDescription(this.getSeoDescription());
        article.as(Seo.ObjectModification.class).setKeywords(this.getSeoKeywords());

        return article;
    }

    @Override
    public Predicate getUniqueFieldPredicate() {
        return null;
    }

    public String getHeadline() {
        return headline;
    }

    public String getSubheadline() {
        return subheadline;
    }

    public WebImageImportTransformer getImageReference() {
        return imageReference;
    }

    public VideoImportTransformer getVideoReference() {
        return videoReference;
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

    public List<SectionPageImportTransformer> getSecondarySectionReferences() {
        if (secondarySectionReferences == null) {
            secondarySectionReferences = new ArrayList<>();
        }
        return secondarySectionReferences;
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
