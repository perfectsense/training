package brightspot.core.requestextras.headelement;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.page.ExternalStyleView;

public class ExternalStyleElementViewModel extends ViewModel<ExternalStyleElementBody> implements ExternalStyleView {

    @Override
    public CharSequence getHref() {
        // Plain text
        return model.getLink();
    }
}
