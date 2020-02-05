package brightspot.core.containermodule;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.container.TwoColumnContainerView;
import com.psddev.styleguide.core.container.TwoColumnContainerViewColumnOneField;
import com.psddev.styleguide.core.container.TwoColumnContainerViewColumnTwoField;

/**
 * A {@link ViewModel} of a {@link TwoColumnContainer} to render a {@link TwoColumnContainerView}.
 */
public class TwoColumnContainerViewModel extends ViewModel<TwoColumnContainer> implements TwoColumnContainerView {

    @Override
    public Iterable<? extends TwoColumnContainerViewColumnOneField> getColumnOne() {
        return createViews(TwoColumnContainerViewColumnOneField.class, model.getColumnOne());
    }

    @Override
    public Iterable<? extends TwoColumnContainerViewColumnTwoField> getColumnTwo() {
        return createViews(TwoColumnContainerViewColumnTwoField.class, model.getColumnTwo());
    }
}
