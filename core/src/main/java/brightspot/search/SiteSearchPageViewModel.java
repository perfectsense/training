package brightspot.search;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.page.AbstractPageViewModel;
import brightspot.page.CurrentPageViewModel;
import brightspot.page.PageViewModel;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.jsonld.JsonLdType;
import com.psddev.dari.util.StringUtils;
import com.psddev.dari.web.UrlBuilder;
import com.psddev.dari.web.annotation.WebParameter;
import com.psddev.styleguide.page.PageViewPageHeadingField;
import com.psddev.styleguide.page.PageViewPageLeadField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import com.psddev.styleguide.search.SearchResultsPageView;
import com.psddev.styleguide.search.SearchResultsPageViewFiltersField;
import com.psddev.styleguide.search.SearchResultsPageViewResultsField;
import com.psddev.styleguide.search.SearchResultsPageViewSortsField;
import com.psddev.styleguide.search.SearchResultsPageViewSpotlightsField;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

@JsonLdType("WebPage")
public class SiteSearchPageViewModel extends AbstractPageViewModel<SiteSearchPage>
    implements SearchResultsPageView, PageEntryView {

    protected static final String VIEW_PARAMETER = "v";

    @CurrentPageViewModel(PageViewModel.class)
    protected PageViewModel page;

    @WebParameter(VIEW_PARAMETER)
    protected String view;

    @WebParameter(SimpleSiteSearchResultBuilder.QUERY_PARAMETER)
    protected String queryString;

    protected String baseUrl;

    protected SiteSearchResults siteSearchResult;

    @Override
    protected void onCreate(ViewResponse response) {
        siteSearchResult = SimpleSiteSearchResultBuilder.create(model)
            .buildResult();

        this.queryString = Optional.ofNullable(queryString)
            .map(input -> Jsoup.clean(input, Whitelist.none()))
            .orElse(null);

        baseUrl = new UrlBuilder().current().build();
    }

    @Override
    public Iterable<? extends PageViewPageHeadingField> getPageHeading() {
        return RichTextUtils.buildInlineHtml(
            model,
            SiteSearchPage::getTitle,
            e -> createView(PageViewPageHeadingField.class, e));
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return RichTextUtils.buildInlineHtml(
            model,
            SiteSearchPage::getDescription,
            e -> createView(PageViewPageSubHeadingField.class, e));
    }

    @Override
    public String getQuery() {
        return queryString;
    }

    @Override
    public Long getPageNumber() {
        return siteSearchResult.getPageNumber();
    }

    @Override
    public Long getCount() {
        return siteSearchResult.getResultsCount();
    }

    @Override
    public Long getPageCount() {
        return siteSearchResult.getPageCount();
    }

    @Override
    public Iterable<? extends SearchResultsPageViewFiltersField> getFilters() {
        return createViews(SearchResultsPageViewFiltersField.class, model.getFilters());
    }

    @Override
    public Iterable<? extends SearchResultsPageViewSortsField> getSorts() {
        if (!Optional.ofNullable(model.getSiteSearchSorting()).map(SiteSearchSorting::shouldDisplay).orElse(false)) {
            return null;
        }

        return createViews(
            SearchResultsPageViewSortsField.class,
            Optional.ofNullable(model.getSiteSearchSorting())
                .map(SiteSearchSorting::getSorts)
                .orElse(Collections.emptyList())
        );
    }

    @Override
    public Iterable<? extends SearchResultsPageViewSpotlightsField> getSpotlights() {

        return siteSearchResult.getPageNumber() <= 1 ? createViews(
            SearchResultsPageViewSpotlightsField.class,
            siteSearchResult.getSpotlights()) : null;
    }

    @Override
    public Iterable<? extends SearchResultsPageViewResultsField> getResults() {
        return createViews(
            SearchResultsPageViewResultsField.class,
            siteSearchResult
                .getResults()
                .stream()
                .map(this::transformItem)
                .collect(Collectors.toList()));
    }

    /**
     * Override this method to process the result items before they're made into Views.
     *
     * @param item item to process
     * @return the untransformed item
     */
    public Object transformItem(Object item) {
        return model.transformSiteSearchResult(item);
    }

    @Override
    public CharSequence getPreviousPage() {
        long previous = siteSearchResult.getPageNumber() - 1;

        return previous > 0
            ? StringUtils.addQueryParameters(
            baseUrl,
            SimpleSiteSearchResultBuilder.PAGE_NUMBER_PARAMETER,
            previous)
            : null;
    }

    @Override
    public CharSequence getNextPage() {
        long next = siteSearchResult.getPageNumber() + 1;

        return next <= siteSearchResult.getPageCount()
            ? StringUtils.addQueryParameters(
            baseUrl,
            SimpleSiteSearchResultBuilder.PAGE_NUMBER_PARAMETER,
            next)
            : null;
    }

    // -- Page support --

    @Override
    public Iterable<? extends PageViewPageLeadField> getPageLead() {
        return null;
    }
}
