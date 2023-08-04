package brightspot.importtransformers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.blog.BlogPostLead;
import brightspot.blog.BlogPostPage;
import brightspot.importapi.ImportTransformer;
import brightspot.importapi.element.ImportElement;
import brightspot.importtransformers.element.ImportElementUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.psddev.cms.db.Seo;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlogPostPageImportTransformer extends ImportTransformer<BlogPostPage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogPostPageImportTransformer.class);

    private static final String BLOG_FIELD = "blog";
    private static final String HEADLINE_FIELD = "headline";
    private static final String SUB_HEADLINE_FIELD = "subheadline";
    private static final String LEAD_IMAGE_FIELD = "leadImage";
    private static final String LEAD_VIDEO_FIELD = "leadVideo";
    private static final String BODY_FIELD = "body";
    private static final String AUTHORS_FIELD = "authors";
    private static final String TAGS_FIELD = "tags";

    private static final String PROMO_IMAGE_FIELD = "promoImage";
    private static final String PROMO_DESCRIPTION_FIELD = "promoDescription";
    private static final String SEO_TITLE_FIELD = "seoTitle";
    private static final String SEO_DESCRIPTION_FIELD = "seoDescription";
    private static final String SEO_KEYWORDS_FIELD = "seoKeywords";

    @JsonProperty(BLOG_FIELD)
    private BlogPageImportTransformer blogReference;

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
    public BlogPostPage transform() throws Exception {

        Preconditions.checkArgument(
            StringUtils.isNotBlank(this.getHeadline()),
            "headline not provided for BlogPage with externalId [" + this.getExternalId() + "]");

        BlogPostPage blogPostPage = (BlogPostPage) this.createNewObject();

        if (this.getBlogReference() != null) {
            Optional.ofNullable(this.getBlogReference().findOrCreate(this))
                .ifPresent(blog -> blogPostPage.asHasBlogWithFieldData().setBlog(blog));
        }

        blogPostPage.setHeadline(ImportElementUtil.processInlineRichText(this.getHeadline(), this));
        blogPostPage.setSubheadline(ImportElementUtil.processInlineRichText(this.getSubheadline(), this));

        Optional.ofNullable(ImportTransformerUtil.retrieveLead(
                this.getVideoReference(),
                this.getImageReference(),
                this))
            .map(BlogPostLead.class::cast)
            .ifPresent(blogPostPage::setLead);

        blogPostPage.setBody(ImportElementUtil.createBody(this.getBodyElements(), this));

        blogPostPage.asHasAuthorsWithFieldData()
            .setAuthors(ImportTransformerUtil.retrieveAuthors(this.getAuthorReferences(), this));

        blogPostPage.asHasTagsWithFieldData()
            .setTags(ImportTransformerUtil.retrieveTags(this.getTagReferences(), this));

        ImportTransformerUtil.setPromoImage(blogPostPage, this.getPromoImageReference(), this);
        blogPostPage.asPagePromotableWithOverridesData()
            .setPromoDescription(ImportElementUtil.processInlineRichText(this.getPromoDescription(), this));

        blogPostPage.asSeoWithFieldsData().setTitle(this.getSeoTitle());
        blogPostPage.asSeoWithFieldsData().setDescription(this.getSeoDescription());
        blogPostPage.as(Seo.ObjectModification.class).setKeywords(this.getSeoKeywords());

        return blogPostPage;
    }

    @Override
    public Predicate getUniqueFieldPredicate() {
        return null;
    }

    public BlogPageImportTransformer getBlogReference() {
        return blogReference;
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
