package brightspot.core.requestextras.headelement;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.page.ExternalScriptView;

public class ExternalScriptElementViewModel extends ViewModel<ExternalScriptElementBody> implements ExternalScriptView {

    @Override
    public Boolean getAsync() {
        return model.isAsync();
    }

    @Override
    public Boolean getDefer() {
        return model.isDefer();
    }

    @Override
    public CharSequence getSrc() {
        // Plain text
        return model.getSrc();
    }
}
