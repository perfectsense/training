package brightspot.importtransformers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.genericpage.GenericPage;
import brightspot.importapi.ImportTransformer;
import brightspot.importapi.element.ImportElement;
import brightspot.importtransformers.element.ImportElementUtil;
import brightspot.module.list.page.PageListModulePlacementInline;
import brightspot.module.list.page.SimplePageItemStream;
import brightspot.module.richtext.RichTextModulePlacementInline;
import brightspot.promo.page.PagePromotable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import com.psddev.cms.db.Seo;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class GenericPageImportTransformer extends ImportTransformer<GenericPage> {

    private static final String DISPLAY_NAME_FIELD = "displayName";
    private static final String INTERNAL_NAME_FIELD = "internalName";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String BODY_FIELD = "body";
    private static final String ARTICLE_PROMOS_FIELD = "articlePromos";
    private static final String SECTION_FIELD = "section";
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

    @JsonProperty(SECTION_FIELD)
    private SectionPageImportTransformer sectionReference;

    @JsonProperty(LEAD_IMAGE_FIELD)
    private WebImageImportTransformer imageReference;

    @JsonProperty(BODY_FIELD)
    private List<ImportElement> bodyElements;

    @JsonProperty(ARTICLE_PROMOS_FIELD)
    private List<ArticleImportTransformer> articlePromos;

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
        return ObjectUtils.firstNonBlank(this.externalId, this.getIdAsString(), this.displayName);
    }

    @Override
    public GenericPage transform() throws Exception {

        Preconditions.checkArgument(
            StringUtils.isNotBlank(this.getDisplayName()),
            "displayName not provided for BlogPage with externalId [" + this.getExternalId() + "]");

        GenericPage page = (GenericPage) this.createNewObject();

        page.setDisplayName(ImportElementUtil.processInlineRichText(this.getDisplayName(), this));

        page.setInternalName(this.getInternalName());

        page.setDescription(ImportElementUtil.processInlineRichText(this.getDescription(), this));

        page.asHasSectionWithFieldData()
            .setSection(ImportTransformerUtil.retrieveSectionPage(this.getSectionReference(), this));

        page.setLead(ImportTransformerUtil.retrieveModuleLead(this.getImageReference(), this));

        Optional.ofNullable(ImportElementUtil.createBody(this.getBodyElements(), this))
            .filter(StringUtils::isNotBlank)
            .ifPresent(body -> {
                RichTextModulePlacementInline richTextModule = new RichTextModulePlacementInline();
                richTextModule.setRichText(body);
                page.getContents().add(richTextModule);
            });

        List<PagePromotable> promos = this.getArticlePromos().stream()
            .map(ref -> ref.findOrCreate(this))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if (!ObjectUtils.isBlank(promos)) {
            SimplePageItemStream itemStream = new SimplePageItemStream();
            itemStream.setItems(promos);

            PageListModulePlacementInline listModule = new PageListModulePlacementInline();
            listModule.setItemStream(itemStream);

            page.getContents().add(listModule);
        }

        ImportTransformerUtil.setPromoImage(page, this.getPromoImageReference(), this);
        page.asPagePromotableWithOverridesData()
            .setPromoDescription(ImportElementUtil.processInlineRichText(this.getPromoDescription(), this));

        page.asSeoWithFieldsData().setTitle(this.getSeoTitle());
        page.asSeoWithFieldsData().setDescription(this.getSeoDescription());
        page.as(Seo.ObjectModification.class).setKeywords(this.getSeoKeywords());

        return page;
    }

    @Override
    public Predicate getUniqueFieldPredicate() {
        if (StringUtils.isBlank(this.getDisplayName())) {
            return null;
        }
        return PredicateParser.Static.parse(
            GenericPage.class.getName() + "/displayName = \"" + this.getDisplayName() + "\"");
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

    public SectionPageImportTransformer getSectionReference() {
        return sectionReference;
    }

    public WebImageImportTransformer getImageReference() {
        return imageReference;
    }

    public List<ImportElement> getBodyElements() {
        if (bodyElements == null) {
            bodyElements = new ArrayList<>();
        }
        return bodyElements;
    }

    public List<ArticleImportTransformer> getArticlePromos() {
        if (articlePromos == null) {
            articlePromos = new ArrayList<>();
        }
        return articlePromos;
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
