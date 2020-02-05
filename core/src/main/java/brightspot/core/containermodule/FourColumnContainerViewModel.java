package brightspot.core.containermodule;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.container.FourColumnContainerView;
import com.psddev.styleguide.core.container.FourColumnContainerViewColumnFourField;
import com.psddev.styleguide.core.container.FourColumnContainerViewColumnOneField;
import com.psddev.styleguide.core.container.FourColumnContainerViewColumnThreeField;
import com.psddev.styleguide.core.container.FourColumnContainerViewColumnTwoField;

/**
 * A {@link ViewModel} of a {@link brightspot.core.containermodule.FourColumnContainer} to render a {@link FourColumnContainerView}.
 */
public class FourColumnContainerViewModel extends ViewModel<FourColumnContainer> implements FourColumnContainerView {

    @Override
    public Iterable<? extends FourColumnContainerViewColumnOneField> getColumnOne() {
        return createViews(FourColumnContainerViewColumnOneField.class, model.getColumnOne());
    }

    @Override
    public Iterable<? extends FourColumnContainerViewColumnTwoField> getColumnTwo() {
        return createViews(FourColumnContainerViewColumnTwoField.class, model.getColumnTwo());
    }

    @Override
    public Iterable<? extends FourColumnContainerViewColumnThreeField> getColumnThree() {
        return createViews(FourColumnContainerViewColumnThreeField.class, model.getColumnThree());
    }

    @Override
    public Iterable<? extends FourColumnContainerViewColumnFourField> getColumnFour() {
        return createViews(FourColumnContainerViewColumnFourField.class, model.getColumnFour());
    }
}