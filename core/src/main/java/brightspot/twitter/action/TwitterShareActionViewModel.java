package brightspot.twitter.action;

import java.util.Optional;

import brightspot.core.action.actionbar.ActionBar;
import brightspot.core.action.actionbar.ActionBarViewModel;
import brightspot.core.share.Shareable;
import brightspot.core.share.ShareableData;
import brightspot.core.social.TwitterSocialService;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpRequestAttribute;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;
import com.psddev.styleguide.twitter.intent.TwitterTweetButtonView;

/**
 * ViewModel for TwitterShareAction. Provides access to all the fields in the request for a twitter share dialog,
 * implements {@link TwitterTweetButtonView}
 */
public class TwitterShareActionViewModel extends ViewModel<TwitterShareAction> implements TwitterTweetButtonView {

    @HttpRequestAttribute(ActionBar.ACTION_BAR_CONTEXT_ATTRIBUTE)
    protected Recordable mainContent;

    @CurrentSite
    protected Site site;

    @Override
    public CharSequence getHashtags() {
        return "";
    }

    @Override
    public CharSequence getText() {
        if (mainContent instanceof Shareable) {
            return StringUtils.encodeUri(mainContent.as(ShareableData.class).getShareTitle());
        } else {
            return "Check out this content I found on " + (site != null
                ? site.getName()
                : "Brightspot"); //TODO: probably set this somewhere
        }
    }

    @Override
    public CharSequence getBody() {
        return "Tweet";
    }

    @Override
    public CharSequence getUrl() {
        TwitterSocialService service = Query.from(TwitterSocialService.class).first();
        return Optional.ofNullable(ActionBarViewModel.getShareActionUrl(service, site, mainContent))
            .map(CharSequence::toString)
            .map(StringUtils::encodeUri)
            .orElse(null);
    }

    @Override
    public CharSequence getVia() {
        return null;
    }
}
