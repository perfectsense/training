package brightspot.core.navigation;

import java.util.Optional;

import brightspot.core.link.InternalLink;
import brightspot.core.link.Link;
import brightspot.core.link.Target;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.navigation.NavigationLinkView;

/**
 * The {@link SubNavigationLinkItemViewModel} provides a {@link ViewModel} for a {@link SubNavigationItem}.
 */
public class SubNavigationLinkItemViewModel
    extends ViewModel<SubNavigationItem>
    implements NavigationLinkView {

    @CurrentSite
    protected Site site;

    protected Optional<NavigationLink> navigationLink;

    @Override
    protected void onCreate(ViewResponse response) {
        super.onCreate(response);
        navigationLink = Optional.ofNullable(this.model)
            .filter(item -> item instanceof NavigationLink)
            .map(NavigationLink.class::cast);
    }

    @Override
    public CharSequence getHref() {

        return navigationLink
            .map(NavigationLink::getLink)
            .map(link -> link.getLinkUrl(site))
            .orElse(null);
    }

    @Override
    public CharSequence getText() {

        return navigationLink
            .map(NavigationLink::getTitleText)
            .orElse(null);
    }

    @Override
    public CharSequence getTarget() {

        return navigationLink
            .map(NavigationLink::getLink)
            .map(Link::getTarget)
            .map(Target::getValue)
            .orElse(null);
    }

    @Override
    public CharSequence getIconUrl() {
        return null;
    }

    @Override
    public CharSequence getContentId() {
        return navigationLink
            .map(NavigationLink::getLink)
            .filter(InternalLink.class::isInstance)
            .map(e -> ((InternalLink) e).getItem())
            .map(e -> e.getState().getId().toString())
            .orElse(null);
    }

    @Override
    public CharSequence getStatus() {
        return null;
    }

    @Override
    public Number getCount() {
        return null;
    }

    @Override
    public Boolean getIsActive() {
        return null;
    }
}
