package brightspot.custompage;

import brightspot.cascading.CascadingPageElements;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.DefaultPermalinkRule;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.search.boost.HasSiteSearchBoostIndexes;
import brightspot.search.modifier.exclusion.SearchExcludable;
import brightspot.seo.SeoWithFields;
import brightspot.site.DefaultSiteMapItem;
import brightspot.urlslug.HasUrlSlugWithField;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.util.Utils;

/**
 * CustomPage is intended to be extended using an Editorial Content Type. It works closely with CustomPageRenderer and
 * CustomPageViewModel to render the header, footer, cascading page elements, etc., as well as opting in to the
 * PagePromotable system.
 */
public abstract class CustomPage extends Content implements
    CascadingPageElements,
    DefaultSiteMapItem,
    HasSiteSearchBoostIndexes,
    HasUrlSlugWithField,
    Page,
    PagePromotableWithOverrides,
    SearchExcludable,
    SeoWithFields {

    @Required
    private String internalName;

    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String displayName;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getLinkableText() {
        return getDisplayName();
    }

    @Override
    public String getSeoTitleFallback() {
        return getDisplayName();
    }

    @Override
    public String getSeoDescriptionFallback() {
        return getDescription();
    }

    @Override
    public String getUrlSlugFallback() {
        return Utils.toNormalized(getDisplayName());
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, DefaultPermalinkRule.class);
    }

    @Override
    public String getPagePromotableTitleFallback() {
        return getDisplayName();
    }

    @Override
    public String getPagePromotableDescriptionFallback() {
        return getDescription();
    }

    @Override
    public String getSiteSearchBoostTitle() {
        return getDisplayName();
    }

    @Override
    public String getSiteSearchBoostDescription() {
        return getDescription();
    }
}
