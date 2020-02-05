package brightspot.core.navigation;

import java.util.Optional;

import brightspot.core.social.SocialService;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.social.SocialLinkView;

/**
 * The {@link SocialNavigationItemViewModel} provides a {@link ViewModel} for a {@link SocialNavigationItem} required
 * for rendering a {@link SocialLinkView} .
 */
public class SocialNavigationItemViewModel extends ViewModel<SocialNavigationItem> implements SocialLinkView {

    @CurrentSite
    protected Site site;

    @Override
    public CharSequence getHref() {
        return Optional.ofNullable(model.getUrl())
            .orElse(model.getUrlFallback(site));
    }

    @Override
    public CharSequence getSocialService() {
        return Optional.ofNullable(model.getSocialService())
            .map(SocialService::getKey)
            .orElse(null);
    }

    @Override
    public CharSequence getBody() {
        return model.getText();
    }
}
