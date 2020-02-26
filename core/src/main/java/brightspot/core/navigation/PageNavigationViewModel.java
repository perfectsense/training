package brightspot.core.navigation;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.navigation.NavigationView;
import com.psddev.styleguide.core.navigation.NavigationViewItemsField;

/**
 * The {@link PageNavigationViewModel} provides a {@link ViewModel} for a {@link PageNavigation}, supplying the
 * information require to render a {@link NavigationView}.
 *
 * @author Peter J. Radics
 * @version 2.0.0
 * @since 2.0.0
 */
public class PageNavigationViewModel
    extends ViewModel<PageNavigation>
    implements NavigationView {

    @Override
    public Iterable<? extends NavigationViewItemsField> getItems() {
        return createViews(
            NavigationViewItemsField.class,
            this.model.getItems());
    }
}
