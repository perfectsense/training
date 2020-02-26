package brightspot.core.module;

import com.psddev.cms.view.DelegateView;
import com.psddev.cms.view.PreviewEntryView;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.PreviewPageView;

/**
 * Simple delegate view to preview {@link Module}s using {@link ModuleTypePreviewViewModel}.
 */
public class ModulePreviewViewModel extends ViewModel<Module> implements
    DelegateView<PreviewPageView>,
    PreviewEntryView {

    @Override
    public PreviewPageView getDelegate() {
        return createView(PreviewPageView.class, model);
    }
}
