package brightspot.core.containermodule;

import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.container.ContainerView;
import com.psddev.styleguide.core.container.ContainerViewRowsField;

/**
 * A {@link ViewModel} of a {@link ContainerModule} to render a {@link ContainerView}.
 */
public class ContainerModuleViewModel extends ViewModel<ContainerModule> implements ContainerView {

    @Override
    public CharSequence getTitle() {
        return model.getTitle();
    }

    @Override
    public CharSequence getDescription() {
        return RichTextUtils.buildInlineHtml(model.getState().getDatabase(), model.getDescription(), this::createView);
    }

    @Override
    public Iterable<? extends ContainerViewRowsField> getRows() {
        return createViews(ContainerViewRowsField.class, model.getRows());
    }
}
