package brightspot.core.navigation;

import java.util.Optional;

import brightspot.core.link.InternalLink;
import brightspot.core.link.Link;
import brightspot.core.link.Target;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.navigation.NavigationLinkView;

/**
 * The {@link NavigationLinkViewModel} provides a {@link ViewModel} for a {@link NavigationLink} required for rendering
 * a {@link NavigationLinkView} .
 *
 * @author Peter J. Radics
 * @version 0.1.0
 * @since 0.1.0
 */
public class NavigationLinkViewModel
    extends ViewModel<NavigationLink>
    implements NavigationLinkView {

    @CurrentSite
    protected Site site;

    @Override
    public CharSequence getHref() {
        return Optional.ofNullable(model)
            .map(NavigationLink::getLink)
            .map(link -> link.getLinkUrl(site))
            .orElse(null);
    }

    @Override
    public CharSequence getText() {
        return this.model.getTitleText();
    }

    @Override
    public CharSequence getTarget() {
        return Optional.ofNullable(model)
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
        return Optional.of(model)
            .map(NavigationLink::getLink)
            .filter(InternalLink.class::isInstance)
            .map(e -> ((InternalLink) e).getItem())
            .map(e -> e.getState().getId().toString())
            .orElse(null);
    }

    @Override
    public Boolean getIsActive() {
        return null;
    }

    @Override
    public CharSequence getStatus() {
        return null;
    }

    @Override
    public Number getCount() {
        return null;
    }

}
