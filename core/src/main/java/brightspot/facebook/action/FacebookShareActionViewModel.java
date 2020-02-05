package brightspot.facebook.action;

import java.util.Optional;

import brightspot.core.action.actionbar.ActionBar;
import brightspot.core.action.actionbar.ActionBarViewModel;
import brightspot.core.social.FacebookSocialService;
import brightspot.facebook.FacebookSettingsModification;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpRequestAttribute;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;
import com.psddev.styleguide.facebook.FacebookShareButtonView;

/**
 * ViewModel for FacebookShareAction. Provides access to all the fields in the request for a facebook share dialog,
 * implements {@link FacebookShareButtonView}
 */
public class FacebookShareActionViewModel extends ViewModel<FacebookShareAction> implements FacebookShareButtonView {

    @HttpRequestAttribute(ActionBar.ACTION_BAR_CONTEXT_ATTRIBUTE)
    protected Recordable mainContent;

    @CurrentSite
    protected Site site;

    private String appID;

    @Override
    protected boolean shouldCreate() {
        appID = SiteSettings.get(site, item -> item.as(FacebookSettingsModification.class).getFacebookAppId());
        return !StringUtils.isBlank(appID);
    }

    @Override
    public CharSequence getFacebookAppId() {
        return appID;
    }

    @Override
    public CharSequence getRedirectUri() {
        return "";
    }

    @Override
    public CharSequence getDisplay() {
        return "popup";
    }

    @Override
    public CharSequence getShareHref() {
        FacebookSocialService service = Query.from(FacebookSocialService.class).first();
        return Optional.ofNullable(ActionBarViewModel.getShareActionUrl(service, site, mainContent))
            .map(CharSequence::toString)
            .map(StringUtils::encodeUri)
            .orElse(null);
    }

    @Override
    public CharSequence getBody() {
        return "Facebook";
    }

}
