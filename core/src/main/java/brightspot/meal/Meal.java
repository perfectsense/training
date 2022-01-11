package brightspot.meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.article.ArticleLead;
import brightspot.cascading.CascadingPageElements;
import brightspot.image.WebImageAsset;
import brightspot.module.recipe.RecipeModulePlacementInline;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.recipe.HasRecipes;
import brightspot.recipe.Recipe;
import brightspot.rte.MediumRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.section.HasSectionWithField;
import brightspot.section.SectionPrefixPermalinkRule;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.site.DefaultSiteMapItem;
import brightspot.tag.HasTagsWithField;
import brightspot.urlslug.HasUrlSlugWithField;
import brightspot.util.MoreStringUtils;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;
import com.psddev.dari.util.Utils;

public class Meal extends Content implements
    CascadingPageElements,
    DefaultSiteMapItem,
    HasRecipes,
    HasSectionWithField,
    HasTagsWithField,
    HasUrlSlugWithField,
    Page,
    PagePromotableWithOverrides,
    SeoWithFields,
    Shareable {

    @Indexed
    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String title;

    @DynamicPlaceholderMethod("getInternalNameFallback")
    private String internalName;

    private ArticleLead lead;

    @ToolUi.RichText(inline = false, toolbar = MediumRichTextToolbar.class)
    private String description;

    @CollectionMinimum(1)
    private List<MealCourse> courses;

    // --- Getters/setters ---

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public ArticleLead getLead() {
        return lead;
    }

    public void setLead(ArticleLead lead) {
        this.lead = lead;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MealCourse> getCourses() {
        if (courses == null) {
            courses = new ArrayList<>();
        }
        return courses;
    }

    public void setCourses(List<MealCourse> courses) {
        this.courses = courses;
    }

    // --- Fallbacks ---

    private String getInternalNameFallback() {
        return RichTextUtils.richTextToPlainText(getTitle());
    }

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, SectionPrefixPermalinkRule.class);
    }

    // --- HasRecipes support ---

    @Override
    public List<Recipe> getRecipes() {
        return getCourses()
            .stream()
            .flatMap(c -> c.getDishes().stream())
            .map(RecipeModulePlacementInline::getRecipe)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    // --- HasUrlSlugWithField support ---

    @Override
    public String getUrlSlugFallback() {
        return Utils.toNormalized(RichTextUtils.richTextToPlainText(getTitle()));
    }

    // --- Linkable support ---

    @Override
    public String getLinkableText() {
        return getPagePromotableTitle();
    }

    // --- PagePromotableWithOverrides support ---

    @Override
    public String getPagePromotableTitleFallback() {
        return getTitle();
    }

    @Override
    public String getPagePromotableDescriptionFallback() {
        return getDescription();
    }

    @Override
    public WebImageAsset getPagePromotableImageFallback() {
        return Optional.ofNullable(getLead())
            .map(ArticleLead::getArticleLeadImage)
            .orElse(null);
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return MoreStringUtils.firstNonBlank(getInternalName(), this::getInternalNameFallback);
    }

    // --- SeoWithFields support ---

    @Override
    public String getSeoTitleFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableTitle());
    }

    @Override
    public String getSeoDescriptionFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableDescription());
    }

    // --- Shareable support ---

    @Override
    public String getShareableTitleFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableTitleFallback());
    }

    @Override
    public String getShareableDescriptionFallback() {
        return RichTextUtils.richTextToPlainText(getPagePromotableDescriptionFallback());
    }

    @Override
    public WebImageAsset getShareableImageFallback() {
        return getPagePromotableImageFallback();
    }
}
