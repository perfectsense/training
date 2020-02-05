package brightspot.amp.google.dfp;

import java.util.List;

import brightspot.core.ad.AdSize;
import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.Recordable;

/**
 * Stub ViewModel for Amp ad modules defined in styleguide. Actual implementation is in the Amp module. This object is
 * instantiated via {@link brightspot.core.page.CurrentPageViewModel} in {@link brightspot.google.dfp.GoogleDfpAdModuleViewModel}.
 */
public class DefaultAmpGoogleDfpAdModuleViewModel extends ViewModel<Recordable> {

    public boolean isAmpEnabled() {
        return false;
    }

    public Number getAmpWidth(List<AdSize> adSizes) {
        return null;
    }

    public Number getAmpHeight(List<AdSize> adSizes) {
        return null;
    }

    public CharSequence getAmpSizes(List<AdSize> adSizes) {
        return null;
    }

    public boolean hasAmpMultiSizeValidation() {
        return false;
    }
}
