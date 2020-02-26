package brightspot.corporate.page;

import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.Recordable;

/**
 * Stub ViewModel for corporate module page elements defined in styleguide. Actual implementation is in the corporate
 * module. This object is instantiated via {@link brightspot.core.page.CurrentPageViewModel} in {@link
 * brightspot.core.page.PageViewModel}.
 */
public class DefaultCorporatePageViewModel extends ViewModel<Recordable> {

    public <T> Iterable<T> getCountries(Class<T> viewClass) {
        return null;
    }

    public <T> Iterable<T> getBrands(Class<T> viewClass) {
        return null;
    }

    public <T> Iterable<T> getProducts(Class<T> viewClass) {
        return null;
    }

    public <T> Iterable<T> getTopics(Class<T> viewClass) {
        return null;
    }

}
