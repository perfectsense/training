package brightspot.core.search;

import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.servlet.HttpParameter;
import com.psddev.dari.util.PageContextFilter;
import com.psddev.dari.util.StringUtils;
import com.psddev.dari.util.UrlBuilder;
import com.psddev.styleguide.core.page.PageViewPageLeadField;
import com.psddev.styleguide.core.search.SearchFilterViewItemsField;
import com.psddev.styleguide.core.search.SearchResultsPageView;
import com.psddev.styleguide.core.search.SearchResultsPageViewFiltersField;
import com.psddev.styleguide.core.search.SearchResultsPageViewResultsField;
import com.psddev.styleguide.core.search.SearchResultsPageViewSortsField;
import com.psddev.styleguide.core.search.SearchResultsPageViewSpotlightsField;

public class SiteSearchPageViewModel extends SiteSearchViewModel implements SearchResultsPageView, PageEntryView {

    protected static final String VIEW_PARAMETER = "v";

    @HttpParameter(VIEW_PARAMETER)
    protected String view;

    protected String baseUrl;

    @Override
    protected void onCreate(ViewResponse response) {
        super.onCreate(response);
        HttpServletRequest request = PageContextFilter.Static.getRequest();
        baseUrl = new UrlBuilder(request)
            .currentScheme()
            .currentHost()
            .currentPath()
            .currentParameters()
            .toString();
    }

    @Override
    public Long getCount() {
        return count;
    }

    @Override
    public Iterable<? extends SearchResultsPageViewFiltersField> getFilters() {
        Iterable<? extends SearchResultsPageViewFiltersField> filters = createViews(
            SearchResultsPageViewFiltersField.class,
            model.getFilters());

        if (filters != null) {
            for (Iterator<? extends SearchResultsPageViewFiltersField> filtersIterator = filters.iterator();
                filtersIterator.hasNext(); ) {
                SearchResultsPageViewFiltersField filter = filtersIterator.next();

                if (!(filter instanceof FilterSearchFilterViewModel)) {
                    continue;
                }

                FilterSearchFilterViewModel vm = (FilterSearchFilterViewModel) filter;

                vm.setSearch(this);

                Iterable<? extends SearchFilterViewItemsField> items = vm.getItems();

                if (items != null) {
                    Iterator<? extends SearchFilterViewItemsField> itemsIterator = items.iterator();

                    if (itemsIterator.hasNext()) {
                        itemsIterator.next();

                        if (itemsIterator.hasNext()) {
                            continue;
                        }
                    }
                }

                filtersIterator.remove();
            }
        }

        return filters;
    }

    @Override
    public CharSequence getNextPage() {
        long next = pageNumber + 1;

        return next <= pageCount
            ? StringUtils.addQueryParameters(baseUrl, PAGE_NUMBER_PARAMETER, next)
            : null;
    }

    @Override
    public Long getPageCount() {
        return pageCount;
    }

    @Override
    public Long getPageNumber() {
        return pageNumber;
    }

    @Override
    public CharSequence getPreviousPage() {
        long previous = pageNumber - 1;

        return previous > 0
            ? StringUtils.addQueryParameters(baseUrl, PAGE_NUMBER_PARAMETER, previous)
            : null;
    }

    @Override
    public String getQuery() {
        return queryString;
    }

    @Override
    public Iterable<? extends SearchResultsPageViewResultsField> getResults() {
        return createViews(SearchResultsPageViewResultsField.class, result.getItems());
    }

    @Override
    public Iterable<? extends SearchResultsPageViewSortsField> getSorts() {
        Iterable<? extends SearchResultsPageViewSortsField> i = createViews(
            SearchResultsPageViewSortsField.class,
            model.getSorts());

        if (i != null) {
            for (SearchResultsPageViewSortsField sort : i) {
                if (sort instanceof SortSearchControlViewModel) {
                    ((SortSearchControlViewModel) sort).setSearch(this);
                }
            }
        }

        return i;
    }

    @Override
    public Iterable<? extends SearchResultsPageViewSpotlightsField> getSpotlights() {
        return pageNumber <= 1 ? createViews(SearchResultsPageViewSpotlightsField.class, spotlights) : null;
    }

    @Override
    public String getView() {
        return view;
    }

    @Override
    public CharSequence getViewUrl() {
        return StringUtils.addQueryParameters(
            path,
            VIEW_PARAMETER, null,
            VIEW_PARAMETER, "");
    }

    @Override
    public CharSequence getPageHeading() {
        return model.getTitle();
    }

    @Override
    public Iterable<? extends PageViewPageLeadField> getPageLead() {
        return null;
    }

    @Override
    public CharSequence getPageSubHeading() {
        return null;
    }
}
