package brightspot.author;

import java.util.Optional;

import brightspot.cascading.CascadingPageElements;
import brightspot.image.WebImage;
import brightspot.image.WebImageAsset;
import brightspot.opengraph.profile.OpenGraphProfile;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.DefaultPermalinkRule;
import brightspot.promo.author.AuthorPromotable;
import brightspot.promo.page.PagePromotable;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.rte.MediumRichTextToolbar;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.search.boost.HasSiteSearchBoostIndexes;
import brightspot.search.modifier.exclusion.SearchExcludable;
import brightspot.search.sortalphabetical.AlphabeticallySortable;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.site.DefaultSiteMapItem;
import brightspot.social.SocialEntity;
import brightspot.urlslug.HasUrlSlugWithField;
import brightspot.util.MoreStringUtils;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Utils;
import org.apache.commons.lang3.StringUtils;

@ToolUi.FieldDisplayOrder({
    "name",
    "firstName",
    "lastName",
    "hasUrlSlug.urlSlug",
    "image",
    "title",
    "affiliation",
    "email",
    "shortBiography",
    "fullBiography",
    "seo.title",
    "seo.suppressSeoDisplayName",
    "seo.description",
    "seo.keywords",
    "seo.robots",
    "ampPage.ampDisabled"
})
@ToolUi.IconName("person")
@Recordable.DisplayName("Author")
public class PersonAuthor extends Content implements
    AlphabeticallySortable,
    Author,
    AuthorPromotable,
    CascadingPageElements,
    DefaultSiteMapItem,
    HasSiteSearchBoostIndexes,
    HasUrlSlugWithField,
    OpenGraphProfile,
    Page,
    PagePromotableWithOverrides,
    SearchExcludable,
    SeoWithFields,
    Shareable,
    SocialEntity {

    @Indexed
    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    protected String name;

    @Indexed
    @ToolUi.CssClass("is-half")
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    protected String firstName;

    @Indexed
    @ToolUi.CssClass("is-half")
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    protected String lastName;

    protected WebImage image;

    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    protected String title;

    @ToolUi.CssClass("is-half")
    @Indexed
    protected String email;

    @ToolUi.RichText(inline = false, toolbar = MediumRichTextToolbar.class)
    protected String fullBiography;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    @ToolUi.Placeholder(dynamicText = "${content.shortBiographyPlaceholder}", editable = true)
    protected String shortBiography;

    @ToolUi.CssClass("is-half")
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String affiliation;

    @DynamicPlaceholderMethod("getAlphabeticalSortFallback")
    private String alphabeticalSortValue;

    /**
     * @return rich text
     */
    public String getName() {
        return name;
    }

    /**
     * @return rich text
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return rich text
     */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return rich text
     */
    public String getFullBiography() {
        return fullBiography;
    }

    /**
     * @return rich text
     */
    public String getShortBiography() {
        if (StringUtils.isBlank(shortBiography)) {
            if (StringUtils.isBlank(fullBiography)) {
                return "";
            }

            return RichTextUtils.getFirstBodyParagraph(fullBiography, false);
        }

        return shortBiography;
    }

    public String getEmail() {
        return email;
    }

    public WebImage getImage() {
        return image;
    }

    /**
     * @return rich text
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(WebImage image) {
        this.image = image;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullBiography(String fullBiography) {
        this.fullBiography = fullBiography;
    }

    public void setShortBiography(String shortBiography) {
        this.shortBiography = shortBiography;
    }

    public String getShortBiographyPlaceholder() {
        return StringUtils.isBlank(fullBiography)
            ? ""
            : RichTextUtils.getFirstBodyParagraph(fullBiography);
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getAlphabeticalSortValue() {
        return alphabeticalSortValue;
    }

    public void setAlphabeticalSortValue(String alphabeticalSortValue) {
        this.alphabeticalSortValue = alphabeticalSortValue;
    }

    private String getNamePlainText() {
        return RichTextUtils.richTextToPlainText(getName());
    }

    @Override
    public String getLinkableText() {
        return getName();
    }

    // --- HasSiteSearchBoostIndexes support ---

    @Override
    public String getSiteSearchBoostTitle() {
        return getName();
    }

    @Override
    public String getSiteSearchBoostDescription() {
        return getShortBiography();
    }

    // --- HasUrlSlug support ---

    @Override
    public String getUrlSlugFallback() {
        return Utils.toNormalized(getNamePlainText());
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
        return getNamePlainText();
    }

    @Override
    public String getShareableDescriptionFallback() {
        return RichTextUtils.richTextToPlainText(getShortBiography());
    }

    @Override
    public WebImageAsset getShareableImageFallback() {
        return getImage();
    }

    // --- PagePromotable support ---

    @Override
    public String getPagePromotableTitleFallback() {
        return getNamePlainText();
    }

    @Override
    public WebImage getPagePromotableImageFallback() {
        return getImage();
    }

    @Override
    public String getPagePromotableDescriptionFallback() {
        return getShortBiography();
    }

    // --- AuthorPromotable support ---

    @Override
    public String getAuthorPromotableName() {
        return getName();
    }

    @Override
    public String getAuthorPromotableJobTitle() {
        return getTitle();
    }

    @Override
    public WebImage getAuthorPromotableImage() {
        return getImage();
    }

    @Override
    public PagePromotable getAuthorPromotablePublication(Site site) {
        return Query.from(PagePromotable.class)
            .where(HasAuthorsData.class.getName() + "/" + HasAuthorsData.AUTHORING_ENTITIES_FIELD + " = ?", this)
            .and("cms.content.publishDate != missing")
            .sortDescending("cms.content.publishDate")
            .first();
    }

    // --- OpenGraphProfile support ---

    @Override
    public String getOpenGraphProfileUsername() {
        return getNamePlainText();
    }

    @Override
    public String getOpenGraphProfileFirstName() {
        return RichTextUtils.richTextToPlainText(getFirstName());
    }

    @Override
    public String getOpenGraphProfileLastName() {
        return RichTextUtils.richTextToPlainText(getLastName());
    }

    @Override
    public String getOpenGraphProfileGender() {
        return null;
    }

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, DefaultPermalinkRule.class);
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return getNamePlainText();
    }

    // --- AlphabeticallySortable support ---

    private String getAlphabeticalSortFallback() {
        return RichTextUtils.richTextToPlainText(MoreStringUtils.firstNonBlankRichText(
            getLastName(),
            this::parseLastNameFromName,
            this::getNamePlainText));
    }

    @Override
    public String getAlphabeticallySortableIndexValue() {
        return Optional.ofNullable(alphabeticalSortValue)
            .filter(val -> !val.isEmpty())
            .orElseGet(this::getAlphabeticalSortFallback);
    }

    private String parseLastNameFromName() {
        String namePlain = getNamePlainText();
        if (!StringUtils.isBlank(namePlain) && namePlain.contains(" ")) {
            String[] split = namePlain.split(" ");
            return split[split.length - 1];
        }
        return null;
    }
}
