package brightspot.core.requestextras.headelement;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.page.InlineStyleView;

public class InlineStyleElementViewModel extends ViewModel<InlineStyleElementBody> implements InlineStyleView {

    @Override
    public CharSequence getStyle() {
        // Plain text
        return model.getBody();
    }
}
