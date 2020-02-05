package brightspot.linkedin.action;

import java.util.Optional;

import brightspot.core.action.actionbar.ActionBar;
import brightspot.core.action.actionbar.ActionBarViewModel;
import brightspot.core.share.Shareable;
import brightspot.core.social.LinkedInSocialService;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpRequestAttribute;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;
import com.psddev.styleguide.linkedin.LinkedInShareButtonView;

/**
 * ViewModel for LinkedInShareAction. Provides access to all the fields in the request for a linkedin share dialog,
 * implements {@link LinkedInShareButtonView}
 */
public class LinkedInShareActionViewModel extends ViewModel<LinkedInShareAction> implements LinkedInShareButtonView {

    @HttpRequestAttribute(ActionBar.ACTION_BAR_CONTEXT_ATTRIBUTE)
    protected Recordable mainContent;

    @CurrentSite
    protected Site site;

    @Override
    public CharSequence getSummary() {
        return StringUtils.encodeUri(mainContent instanceof Shareable ? ((Shareable) mainContent).asShareableData()
            .getShareDescription() : null);
    }

    @Override
    public CharSequence getSource() {
        return StringUtils.encodeUri(ObjectUtils.isBlank(site) ? null : site.getName());
    }

    @Override
    public CharSequence getTitle() {
        return StringUtils.encodeUri(mainContent instanceof Shareable ? ((Shareable) mainContent).asShareableData()
            .getShareTitle() : null);
    }

    @Override
    public CharSequence getBody() {
        return "LinkedIn";
    }

    @Override
    public CharSequence getUrl() {
        LinkedInSocialService service = Query.from(LinkedInSocialService.class).first();
        return Optional.ofNullable(ActionBarViewModel.getShareActionUrl(service, site, mainContent))
            .map(CharSequence::toString)
            .map(StringUtils::encodeUri)
            .orElse(null);
    }
}
