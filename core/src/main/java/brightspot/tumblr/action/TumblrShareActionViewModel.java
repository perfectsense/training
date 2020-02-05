package brightspot.tumblr.action;

import java.util.Optional;

import brightspot.core.action.actionbar.ActionBar;
import brightspot.core.action.actionbar.ActionBarViewModel;
import brightspot.core.social.TumblrSocialService;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpRequestAttribute;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;
import com.psddev.styleguide.tumblr.TumblrShareButtonView;

/**
 * ViewModel for TumblrShareAction. Provides access to all the fields in the request for a tumblr share dialog,
 * implements {@link TumblrShareButtonView}
 */
public class TumblrShareActionViewModel extends ViewModel<TumblrShareAction> implements TumblrShareButtonView {

    @HttpRequestAttribute(ActionBar.ACTION_BAR_CONTEXT_ATTRIBUTE)
    protected Recordable mainContent;

    @CurrentSite
    protected Site site;

    @Override
    public CharSequence getBody() {
        return "Tumblr";
    }

    @Override
    public CharSequence getUrl() {
        TumblrSocialService service = Query.from(TumblrSocialService.class).first();
        return Optional.ofNullable(ActionBarViewModel.getShareActionUrl(service, site, mainContent))
            .map(CharSequence::toString)
            .map(StringUtils::encodeUri)
            .orElse(null);
    }
}
