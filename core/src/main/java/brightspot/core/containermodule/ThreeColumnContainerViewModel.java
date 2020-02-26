package brightspot.core.containermodule;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.container.ThreeColumnContainerView;
import com.psddev.styleguide.core.container.ThreeColumnContainerViewColumnOneField;
import com.psddev.styleguide.core.container.ThreeColumnContainerViewColumnThreeField;
import com.psddev.styleguide.core.container.ThreeColumnContainerViewColumnTwoField;

/**
 * A {@link ViewModel} of a {@link ThreeColumnContainer} to render a {@link ThreeColumnContainerView}.
 */
public class ThreeColumnContainerViewModel extends ViewModel<ThreeColumnContainer> implements ThreeColumnContainerView {

    @Override
    public Iterable<? extends ThreeColumnContainerViewColumnOneField> getColumnOne() {
        return createViews(ThreeColumnContainerViewColumnOneField.class, model.getColumnOne());
    }

    @Override
    public Iterable<? extends ThreeColumnContainerViewColumnTwoField> getColumnTwo() {
        return createViews(ThreeColumnContainerViewColumnTwoField.class, model.getColumnTwo());
    }

    @Override
    public Iterable<? extends ThreeColumnContainerViewColumnThreeField> getColumnThree() {
        return createViews(ThreeColumnContainerViewColumnThreeField.class, model.getColumnThree());
    }
}
