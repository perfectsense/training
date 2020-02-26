package brightspot.core.navigation;

import java.util.Optional;

import brightspot.core.social.SocialEntityData;
import brightspot.core.social.SocialService;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.CmsTool;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Application;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;

/**
 * A social link navigation (or sub-navigation) item class.
 */
@Recordable.Embedded
public class SocialNavigationItem extends Record implements
    NavigationItem,
    SubNavigationItem {

    @Required
    @ToolUi.DropDown
    private SocialService socialService;

    @ToolUi.Placeholder(dynamicText = "${content.textFallback}", editable = true)
    private String text;

    @ToolUi.Placeholder(dynamicText = "${content.getUrlPlaceholder(toolPageContext)}", editable = true)
    private String url;

    @Relocate
    public SocialService getSocialService() {
        if (socialService == null) {
            socialService = SocialService.getServiceByName(ObjectUtils.to(
                String.class,
                this.getState().getByPath("socialService")));
        }
        return socialService;
    }

    public void setSocialService(SocialService socialService) {
        this.socialService = socialService;
    }

    /**
     * @return designated or default display text for the {@link NavigationLink}
     */
    public String getText() {
        return Optional.ofNullable(text)
            .orElse(getTextFallback());
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTextFallback() {
        return Optional.ofNullable(socialService)
            .map(SocialService::getDisplayName)
            .orElse(null);
    }

    /**
     * @param site A {@link Site} object
     * @return A configured social network URL from SiteSettings, given the configured {@code socialService}, if
     * available.
     */
    public String getUrlFallback(Site site) {
        String url = null;
        SiteSettings siteSettings = site != null
            ? site
            : Application.Static.getInstance(CmsTool.class);

        if (siteSettings != null && socialService != null) {
            url = socialService.getUrl(siteSettings.as(SocialEntityData.class));
        }

        return url;
    }

    public String getUrlPlaceholder(ToolPageContext toolPageContext) {
        if (!StringUtils.isBlank(url)) {
            return null;
        }

        return toolPageContext != null
            ? getUrlFallback(toolPageContext.getSite())
            : null;
    }

    @Override
    public String getLabel() {
        return getText();
    }
}
