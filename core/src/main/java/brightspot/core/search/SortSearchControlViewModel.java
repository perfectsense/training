package brightspot.core.search;

import com.psddev.cms.view.ViewModel;
import com.psddev.dari.util.StringUtils;
import com.psddev.styleguide.core.search.SearchControlView;

public class SortSearchControlViewModel extends ViewModel<Sort> implements SearchControlView {

    private SiteSearchViewModel search;

    public SiteSearchViewModel getSearch() {
        return search;
    }

    public void setSearch(SiteSearchViewModel search) {
        this.search = search;
    }

    @Override
    public CharSequence getLabel() {
        // Plain text
        return model.getLabel();
    }

    @Override
    public Boolean getSelected() {
        return model.equals(search.getSort());
    }

    @Override
    public CharSequence getUrl() {
        // Plain text
        return StringUtils.addQueryParameters(
            search.getPath(),
            SiteSearchViewModel.SORT_PARAMETER,
            model.getParameterValue(),
            SiteSearchViewModel.PAGE_NUMBER_PARAMETER,
            null);
    }

    @Override
    public Number getCount() {
        return null;
    }

    @Override
    public CharSequence getParameterValue() {
        return model.getParameterValue();
    }
}
