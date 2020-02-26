package brightspot.core.navigation;

import java.util.Optional;

import brightspot.core.link.InternalLink;
import brightspot.core.link.Link;
import brightspot.core.link.Target;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.MainObject;
import com.psddev.dari.db.State;
import com.psddev.styleguide.core.navigation.NavigationItemView;
import com.psddev.styleguide.core.navigation.NavigationItemViewItemsField;

/**
 * The {@link TopNavigationItemViewModel} provides a {@link ViewModel} for a {@link TopNavigationItem}.
 */
public class TopNavigationItemViewModel extends ViewModel<TopNavigationItem> implements NavigationItemView {

    @CurrentSite
    protected Site site;

    @MainObject
    private Object mainObject;

    @Override
    public CharSequence getHref() {
        NavigationItemTitle title = model.getTitle();

        if (title instanceof NavigationLink) {
            Link link = ((NavigationLink) title).getLink();
            return link != null ? link.getLinkUrl(site) : null;
        }
        return null;
    }

    @Override
    public CharSequence getTarget() {
        return Optional.ofNullable(model.getTitle())
            .filter(title -> title instanceof NavigationLink)
            .map(NavigationLink.class::cast)
            .map(NavigationLink::getLink)
            .map(Link::getTarget)
            .map(Target::getValue)
            .orElse(null);
    }

    @Override
    public CharSequence getText() {
        NavigationItemTitle title = model.getTitle();
        return title != null ? title.getTitleText() : null;
    }

    @Override
    public Boolean getIsCurrent() {
        return Optional.ofNullable(mainObject)
            .map(State::getInstance)
            .map(State::getId)
            .map(Object::toString)
            .map(id -> Optional.ofNullable(getContentId()).map(id::equals).orElse(false))
            .orElse(null);
    }

    @Override
    public Iterable<? extends NavigationItemViewItemsField> getItems() {
        return createViews(
            NavigationItemViewItemsField.class,
            model.getSubNavigation());
    }

    @Override
    public CharSequence getContentId() {
        return Optional.ofNullable(model.getTitle())
            .filter(NavigationLink.class::isInstance)
            .map(e -> ((NavigationLink) e).getLink())
            .filter(InternalLink.class::isInstance)
            .map(e -> ((InternalLink) e).getItem())
            .map(e -> e.getState().getId().toString())
            .orElse(null);
    }

    @Override
    public CharSequence getIconUrl() {
        return null;
    }

}
