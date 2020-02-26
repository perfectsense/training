package brightspot.core.requestextras.headelement;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.page.InlineScriptView;

public class InlineScriptElementViewModel extends ViewModel<InlineScriptElementBody> implements InlineScriptView {

    @Override
    public CharSequence getScript() {
        // Plain text
        return model.getBody();
    }
}
