package brightspot.importtransformers;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import brightspot.importapi.ImportTransformer;
import brightspot.importtransformers.element.ImportElementUtil;
import brightspot.tag.TagPage;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.psddev.cms.db.Seo;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class TagPageImportTransformer extends ImportTransformer<TagPage> {

    private static final String DISPLAY_NAME_FIELD = "displayName";
    private static final String INTERNAL_NAME_FIELD = "internalName";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String PARENT_FIELD = "parent";
    private static final String LEAD_IMAGE_FIELD = "leadImage";
    private static final String HIDE_TAG_FIELD = "hidden";

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
    private TagPageImportTransformer parent;

    @JsonProperty(LEAD_IMAGE_FIELD)
    private WebImageImportTransformer imageReference;

    @JsonProperty(HIDE_TAG_FIELD)
    private Boolean hideTag;

    @JsonProperty(SEO_TITLE_FIELD)
    private String seoTitle;

    @JsonProperty(SEO_DESCRIPTION_FIELD)
    private String seoDescription;

    @JsonProperty(SEO_KEYWORDS_FIELD)
    private Set<String> seoKeywords;

    @Override
    public TagPage transform() throws Exception {

        Preconditions.checkArgument(
            StringUtils.isNotBlank(this.getDisplayName()),
            "displayName not provided for TagPage with externalId [" + this.getExternalId() + "]");

        TagPage tag = (TagPage) this.createNewObject();

        tag.setDisplayName(ImportElementUtil.processInlineRichText(this.getDisplayName(), this));

        tag.setInternalName(this.getInternalName());

        tag.setDescription(ImportElementUtil.processInlineRichText(this.getDescription(), this));

        if (this.getParent() != null) {
            Optional.ofNullable(this.getParent().findOrCreate(this))
                .ifPresent(tag::setParent);
        }

        tag.setLead(ImportTransformerUtil.retrieveModuleLead(this.getImageReference(), this));

        if (this.getHideTag()) {
            tag.setHidden(true);
        }

        tag.asSeoWithFieldsData().setTitle(this.getSeoTitle());
        tag.asSeoWithFieldsData().setDescription(this.getSeoDescription());
        tag.as(Seo.ObjectModification.class).setKeywords(this.getSeoKeywords());

        return tag;
    }

    @Override
    public Predicate getUniqueFieldPredicate() {
        if (StringUtils.isBlank(this.getDisplayName())) {
            return null;
        }
        return PredicateParser.Static.parse(
            TagPage.class.getName() + "/tag.getTagDisplayNamePlainText = \"" + this.getDisplayName() + "\"");
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

    public TagPageImportTransformer getParent() {
        return parent;
    }

    public WebImageImportTransformer getImageReference() {
        return imageReference;
    }

    public void setImageReference(WebImageImportTransformer imageReference) {
        this.imageReference = imageReference;
    }

    public boolean getHideTag() {
        return Boolean.TRUE.equals(hideTag);
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
