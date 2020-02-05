package brightspot.core.background;

import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.ViewModelOverlayValueEntryView;
import com.psddev.styleguide.core.background.BackgroundView;

public class BackgroundViewModel extends ViewModel<Background>
    implements ViewModelOverlayValueEntryView, BackgroundView {

    @Override
    public CharSequence getCssValue() {
        return model.getCssValue();
    }
}
