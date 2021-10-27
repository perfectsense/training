package brightspot.author;

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
import brightspot.search.modifier.exclusion.SearchExcludable;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.site.DefaultSiteMapItem;
import brightspot.social.SocialEntity;
import brightspot.urlslug.HasUrlSlugWithField;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Utils;
import org.apache.commons.lang3.StringUtils;

@ToolUi.FieldDisplayOrder({
    "name",
    "firstName",
    "lastName",
    "image",
    "title",
    "affiliation",
    "email",
    "shortBiography",
    "fullBiography"
})
@ToolUi.IconName("person")
@Recordable.DisplayName("Author")
public class PersonAuthor extends Content implements
        Author,
        AuthorPromotable,
        CascadingPageElements,
        DefaultSiteMapItem,
        HasUrlSlugWithField,
        OpenGraphProfile,
        Page,
        PagePromotableWithOverrides,
        SearchExcludable,
        SeoWithFields,
        Shareable,
        SocialEntity {

    public static final String PAGE_PROMOTABLE_TYPE = "author";

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
        return StringUtils.isBlank(shortBiography)
                ? getShortBiographyPlaceholder()
                : shortBiography;
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

    @Override
    public String getLinkableText() {
        return getName();
    }

    // --- HasUrlSlug support ---

    @Override
    public String getUrlSlugFallback() {
        return Utils.toNormalized(getName());
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
        return RichTextUtils.richTextToPlainText(getName());
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
        return getName();
    }

    @Override
    public WebImage getPagePromotableImageFallback() {
        return getImage();
    }

    @Override
    public String getPagePromotableDescriptionFallback() {
        return getShortBiography();
    }

    @Override
    public String getPagePromotableType() {
        return PAGE_PROMOTABLE_TYPE;
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
        return RichTextUtils.richTextToPlainText(getName());
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
        return RichTextUtils.richTextToPlainText(getName());
    }
}
