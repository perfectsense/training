package brightspot.core.person;

import brightspot.core.image.ImageOption;
import brightspot.core.link.Linkable;
import brightspot.core.page.Page;
import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.permalink.DefaultPermalinkRule;
import brightspot.core.promo.PromotableWithOverrides;
import brightspot.core.readreceipt.Readable;
import brightspot.core.site.ExpressSiteMapItem;
import brightspot.core.social.SocialEntity;
import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.util.ObjectUtils;

@ToolUi.FieldDisplayOrder({
    "name",
    "firstName",
    "lastName",
    "image",
    "title",
    "affiliation",
    "email",
    "shortBiography",
    "fullBiography",
    // Main Tab
    "promotable.promoTitle",
    "promotable.promoDescription",
    "promotable.promoImage",
    "page.layoutOption" }) // Overrides Tab
@Seo.TitleFields("getSeoTitle")
@Seo.DescriptionFields("getSeoDescription")
@ToolUi.IconName("person")
public class Author extends AbstractPerson implements
    Directory.Item,
    ExpressSiteMapItem,
    Linkable,
    Page,
    PersonOrCurrentPerson,
    PromotableWithOverrides,
    Readable,
    SocialEntity {

    public static final String TYPE = "author";

    private String affiliation;

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    @Override
    public String getLinkableText() {
        return ObjectUtils.firstNonBlank(this.name, this.getLabel());
    }

    @Override
    public String getPromotableType() {
        return TYPE;
    }

    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoTitle() {
        return getPromotableTitle();
    }

    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoDescription() {
        return getPromotableDescription();
    }

    /* Promotable Implementation */

    @Override
    public String getPromotableTitleFallback() {
        return getName();
    }

    @Override
    public ImageOption getPromotableImageFallback() {
        return getImage();
    }

    @Override
    public String getPromotableDescriptionFallback() {
        return RichTextUtils.richTextToPlainText(getShortBiography());
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, DefaultPermalinkRule.class);
    }
}
