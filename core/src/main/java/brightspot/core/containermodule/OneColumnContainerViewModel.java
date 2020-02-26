package brightspot.core.containermodule;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.container.OneColumnContainerView;
import com.psddev.styleguide.core.container.OneColumnContainerViewColumnOneField;

/**
 * A {@link ViewModel} of a {@link brightspot.core.containermodule.OneColumnContainer} to render a {@link OneColumnContainerView}.
 */
public class OneColumnContainerViewModel extends ViewModel<OneColumnContainer> implements OneColumnContainerView {

    @Override
    public Iterable<? extends OneColumnContainerViewColumnOneField> getColumnOne() {
        return createViews(OneColumnContainerViewColumnOneField.class, model.getColumnOne());
    }
}