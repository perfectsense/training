package brightspot.amp.google.adsense;

import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.Recordable;

/**
 * Stub ViewModel for Amp ad modules defined in styleguide. Actual implementation is in the Amp module. This object is
 * instantiated via {@link brightspot.core.page.CurrentPageViewModel} in {@link brightspot.google.adsense.GoogleAdSenseAdModuleViewModel}.
 */
public class DefaultAmpGoogleAdSenseViewModel extends ViewModel<Recordable> {

    public boolean isAmpEnabled() {
        return false;
    }
}
