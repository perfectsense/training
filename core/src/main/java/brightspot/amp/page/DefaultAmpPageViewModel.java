package brightspot.amp.page;

import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.dari.db.Recordable;

/**
 * Stub ViewModel for Amp module page elements defined in styleguide. Actual implementation is in the Amp module. This
 * object is instantiated via {@link brightspot.core.page.CurrentPageViewModel} in {@link
 * brightspot.core.page.PageViewModel}.
 */
public class DefaultAmpPageViewModel extends ViewModel<Recordable> {

    @CurrentSite
    protected Site site;

    public <T> Iterable<T> getAmpIntegrations(Class<T> viewClass) {
        return null;
    }

    public <T> Iterable<T> getAmpCustomAnalytics(Class<T> viewClass) {
        return null;
    }
}
