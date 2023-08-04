package brightspot.importtransformers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import brightspot.importapi.ImportTransformer;
import brightspot.importtransformers.element.ImportElementUtil;
import brightspot.section.SectionPage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.psddev.cms.db.Seo;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class SectionPageImportTransformer extends ImportTransformer<SectionPage> {

    private static final String DISPLAY_NAME_FIELD = "displayName";
    private static final String INTERNAL_NAME_FIELD = "internalName";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String PARENT_FIELD = "parent";
    private static final String LEAD_IMAGE_FIELD = "leadImage";

    private static final String PROMO_IMAGE_FIELD = "promoImage";
    private static final String PROMO_DESCRIPTION_FIELD = "promoDescription";
    private static final String SEO_TITLE_FIELD = "seoTitle";
    private static final String SEO_DESCRIPTION_FIELD = "seoDescription";
    private static final String SEO_KEYWORDS_FIELD = "seoKeywords";

    @JsonProperty(DISPLAY_NAME_FIELD)
    private String displayName;

    @JsonProperty(INTERNAL_NAME_FIELD)
    private String internalName;

    @JsonProperty(DESCRIPTION_FIELD)
    private String description;

    @JsonProperty(PARENT_FIELD)
    private SectionPageImportTransformer parent;

    @JsonProperty(LEAD_IMAGE_FIELD)
    private WebImageImportTransformer imageReference;

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
    public SectionPage transform() throws Exception {

        Preconditions.checkArgument(
            StringUtils.isNotBlank(this.getDisplayName()),
            "displayName not provided for SectionPage with externalId [" + this.getExternalId() + "]");

        SectionPage section = (SectionPage) this.createNewObject();

        section.setDisplayName(ImportElementUtil.processInlineRichText(this.getDisplayName(), this));

        section.setInternalName(this.getInternalName());

        section.setDescription(ImportElementUtil.processInlineRichText(this.getDescription(), this));

        Optional.ofNullable(this.getParent())
            .map(p -> p.findOrCreate(this))
            .ifPresent(section::setParent);

        section.setLead(ImportTransformerUtil.retrieveModuleLead(this.getImageReference(), this));

        ImportTransformerUtil.setPromoImage(section, this.getPromoImageReference(), this);
        section.asPagePromotableWithOverridesData()
            .setPromoDescription(ImportElementUtil.processInlineRichText(this.getPromoDescription(), this));

        section.asSeoWithFieldsData().setTitle(this.getSeoTitle());
        section.asSeoWithFieldsData().setDescription(this.getSeoDescription());
        section.as(Seo.ObjectModification.class).setKeywords(this.getSeoKeywords());

        return section;
    }

    @Override
    public Predicate getUniqueFieldPredicate() {
        if (StringUtils.isBlank(this.getDisplayName())) {
            return null;
        }
        return PredicateParser.Static.parse(
            SectionPage.class.getName() + "/displayName = \"" + this.getDisplayName() + "\"");
    }

    @Override
    public String getExternalId() {
        return ObjectUtils.firstNonBlank(this.externalId, this.getIdAsString(), this.displayName);
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getInternalName() {
        return internalName;
    }

    public String getDescription() {
        return description;
    }

    public SectionPageImportTransformer getParent() {
        return parent;
    }

    public WebImageImportTransformer getImageReference() {
        return imageReference;
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
