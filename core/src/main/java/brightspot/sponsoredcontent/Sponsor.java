package brightspot.sponsoredcontent;

import java.util.Locale;
import java.util.Optional;

import brightspot.image.WebImageAsset;
import brightspot.link.Link;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Localization;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.l10n.request.web.LocalizationRequest;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.cms.ui.form.DynamicPlaceholderMethod;
import com.psddev.cms.ui.form.EditablePlaceholder;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.web.WebRequest;

public class Sponsor extends Content implements ContentSponsor {

    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String displayName;

    @EditablePlaceholder
    @DynamicPlaceholderMethod("getInternalNameFallback")
    private String internalName;

    private WebImageAsset logo;

    @Deprecated
    private String description;

    @DisplayName("Sponsor Link")
    private Link callToAction;

    @ToolUi.Tab("Overrides")
    @DynamicPlaceholderMethod("getDisplayTextPlaceholder")
    private String sponsorDisplayText;

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getInternalName() {
        return Optional.ofNullable(internalName)
            .orElseGet(this::getInternalNameFallback);
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    @Override
    public WebImageAsset getLogo() {
        return logo;
    }

    public void setLogo(WebImageAsset logo) {
        this.logo = logo;
    }

    @Deprecated
    @Override
    public String getDescription() {
        return description;
    }

    @Deprecated
    public void setDescription(String description) {
        this.description = description;
    }

    public Link getCallToAction() {
        return callToAction;
    }

    public void setCallToAction(Link callToAction) {
        this.callToAction = callToAction;
    }

    @Override
    public String getSponsorDisplayText() {
        return sponsorDisplayText;
    }

    public void setSponsorDisplayText(String sponsorDisplayText) {
        this.sponsorDisplayText = sponsorDisplayText;
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {
        return getInternalName();
    }

    // --- Fallback ----

    private String getInternalNameFallback() {
        if (!ObjectUtils.isBlank(getDisplayName())) {
            return RichTextUtils.richTextToPlainText(getDisplayName());
        }
        return "";
    }

    private String getSponsorDisplayTextFallback(Site site) {
        String sponsorDisplayTextSiteSettings = SiteSettings.get(
            site,
            s -> s.as(SponsoredContentSiteSettings.class).getSponsorDisplayText());

        Locale locale = null;
        if (WebRequest.isAvailable()) {
            locale = WebRequest.getCurrent().as(LocalizationRequest.class).getLocale();
        }

        String sponsorDisplayTextDefault = Localization.text(locale, "sponsorDisplayText", "Sponsored By");

        return Optional.ofNullable(sponsorDisplayTextSiteSettings)
            .orElse(sponsorDisplayTextDefault);
    }

    public String getSponsorDisplayTextWithFallback(Site site) {
        return Optional.ofNullable(getSponsorDisplayText())
            .orElseGet(() -> getSponsorDisplayTextFallback(site));
    }

    // --- Dynamic Placeholders ---

    private String getDisplayTextPlaceholder() {
        Site site = WebRequest.getCurrent().as(ToolRequest.class).getCurrentSite();
        return getSponsorDisplayTextFallback(site);
    }
}
