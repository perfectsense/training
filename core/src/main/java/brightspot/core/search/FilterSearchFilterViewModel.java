package brightspot.core.search;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.search.SearchFilterView;
import com.psddev.styleguide.core.search.SearchFilterViewItemsField;

public class FilterSearchFilterViewModel extends ViewModel<Filter> implements SearchFilterView {

    private SiteSearchViewModel search;
    private boolean itemsInitialized;
    private Iterable<? extends SearchFilterViewItemsField> items;

    public SiteSearchViewModel getSearch() {
        return search;
    }

    public void setSearch(SiteSearchViewModel search) {
        this.search = search;
    }

    @Override
    public CharSequence getHeading() {
        // Plain text
        return model.getHeading();
    }

    @Override
    public CharSequence getParameterName() {
        return model.getParameterName();
    }

    @Override
    public Iterable<? extends SearchFilterViewItemsField> getItems() {
        if (!itemsInitialized) {
            itemsInitialized = true;

            if (model instanceof TypeFilter || !model.isExcludeFromAllTypes() || !search.isAllTypesSelected()) {
                items = createViews(
                    SearchFilterViewItemsField.class,
                    model.getFilterItems(
                        search,
                        search.getFilterValues().get(model.getParameterName())));
            }
        }

        return items;
    }
}
