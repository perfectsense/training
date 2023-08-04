package brightspot.person;

import java.util.Optional;

import brightspot.cascading.CascadingPageElements;
import brightspot.image.WebImage;
import brightspot.image.WebImageAsset;
import brightspot.opengraph.profile.OpenGraphProfile;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.DefaultPermalinkRule;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.promo.person.PersonPromotable;
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
@Recordable.DisplayName("Person")
public class PersonPage extends Content implements
    AlphabeticallySortable,
    CascadingPageElements,
    DefaultSiteMapItem,
    HasSiteSearchBoostIndexes,
    HasUrlSlugWithField,
    OpenGraphProfile,
    Page,
    PagePromotableWithOverrides,
    Person,
    PersonPromotable,
    SearchExcludable,
    SeoWithFields,
    Shareable,
    SocialEntity {

    public static final String PAGE_PROMOTABLE_TYPE = "person";

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public WebImage getImage() {
        return image;
    }

    public void setImage(WebImage image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullBiography() {
        return fullBiography;
    }

    public void setFullBiography(String fullBiography) {
        this.fullBiography = fullBiography;
    }

    public String getShortBiography() {
        return StringUtils.isBlank(shortBiography)
            ? getShortBiographyPlaceholder()
            : shortBiography;
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

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, DefaultPermalinkRule.class);
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

    // --- PagePromotable support ---

    @Override
    public String getPagePromotableType() {
        return PAGE_PROMOTABLE_TYPE;
    }

    @Override
    public String getPagePromotableTitleFallback() {
        return getNamePlainText();
    }

    @Override
    public String getPagePromotableDescriptionFallback() {
        return getShortBiography();
    }

    @Override
    public WebImageAsset getPagePromotableImageFallback() {
        return getImage();
    }

    // --- Person support ---

    @Override
    public String getPersonName() {
        return getName();
    }

    @Override
    public WebImageAsset getPersonImage() {
        return getImage();
    }

    @Override
    public String getPersonShortBiography() {
        return getShortBiography();
    }

    // --- PersonPromotable support ---

    @Override
    public String getPersonPromotableName() {
        return getName();
    }

    @Override
    public String getPersonPromotableJobTitle() {
        return getTitle();
    }

    @Override
    public WebImageAsset getPersonPromotableImage() {
        return getImage();
    }

    @Override
    public String getPersonPromotableDescription() {
        return getShortBiography();
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return getNamePlainText();
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

    private String getNamePlainText() {
        return RichTextUtils.richTextToPlainText(getName());
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
