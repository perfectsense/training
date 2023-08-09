package brightspot.importtransformers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.importapi.ImportTransformer;
import brightspot.importapi.element.ImportElement;
import brightspot.importtransformers.element.ImportElementUtil;
import brightspot.pressrelease.PressRelease;
import brightspot.pressrelease.PressReleaseLead;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.psddev.cms.db.Seo;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class PressReleaseImportTransformer extends ImportTransformer<PressRelease> {

    private static final String HEADLINE_FIELD = "headline";
    private static final String SUB_HEADLINE_FIELD = "subheadline";
    private static final String LEAD_IMAGE_FIELD = "leadImage";
    private static final String LEAD_VIDEO_FIELD = "leadVideo";
    private static final String BODY_FIELD = "body";
    private static final String SECTION_FIELD = "section";
    private static final String SECONDARY_SECTIONS_FIELD = "secondarySections";
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
    public PressRelease transform() throws Exception {

        Preconditions.checkArgument(
            StringUtils.isNotBlank(this.getHeadline()),
            "headline not provided for PressRelease with externalId [" + this.getExternalId() + "]");

        PressRelease pressRelease = (PressRelease) this.createNewObject();

        pressRelease.setHeadline(ImportElementUtil.processInlineRichText(this.getHeadline(), this));
        pressRelease.setSubHeadline(ImportElementUtil.processInlineRichText(this.getSubheadline(), this));

        Optional.ofNullable(ImportTransformerUtil.retrieveLead(
                this.getVideoReference(),
                this.getImageReference(),
                this))
            .map(PressReleaseLead.class::cast)
            .ifPresent(pressRelease::setLead);

        pressRelease.setBody(ImportElementUtil.createBody(this.getBodyElements(), this));

        pressRelease.asHasSectionWithFieldData()
            .setSection(ImportTransformerUtil.retrieveSectionPage(this.getSectionReference(), this));
        pressRelease.asHasSecondarySectionsWithFieldData()
            .setSecondarySections(ImportTransformerUtil.retrieveSecondarySections(
                this.getSecondarySectionReferences(),
                this));

        pressRelease.asHasTagsWithFieldData()
            .setTags(ImportTransformerUtil.retrieveTags(this.getTagReferences(), this));

        ImportTransformerUtil.setPromoImage(pressRelease, this.getPromoImageReference(), this);
        pressRelease.asPagePromotableWithOverridesData()
            .setPromoDescription(ImportElementUtil.processInlineRichText(this.getPromoDescription(), this));

        pressRelease.asSeoWithFieldsData().setTitle(this.getSeoTitle());
        pressRelease.asSeoWithFieldsData().setDescription(this.getSeoDescription());
        pressRelease.as(Seo.ObjectModification.class).setKeywords(this.getSeoKeywords());

        return pressRelease;
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
