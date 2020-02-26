package brightspot.core.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;

import brightspot.core.page.AbstractPageViewModel;
import brightspot.core.promo.Promo;
import com.google.common.collect.ImmutableSet;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpParameter;
import com.psddev.dari.db.CompoundPredicate;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.QueryPhrase;
import com.psddev.dari.util.CompactMap;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PageContextFilter;
import com.psddev.dari.util.PaginatedResult;
import com.psddev.dari.util.UrlBuilder;
import org.apache.commons.lang3.StringUtils;

public abstract class SiteSearchViewModel extends AbstractPageViewModel<SiteSearch> {

    /**
     * Key for HttpServletRequest attribute whose value is the query object that gets the results for this site search.
     */
    public static final String QUERY_ATTRIBUTE = "searchQuery";
    protected static final String QUERY_PARAMETER = "q";
    protected static final String SORT_PARAMETER = "s";
    protected static final String PAGE_NUMBER_PARAMETER = "p";
    protected static final Pattern TOKENIZATION_PATTERN = Pattern.compile("\\s+");
    protected static final Pattern QUOTATION_PATTERN = Pattern.compile("\"([^\"]+)\"");

    @CurrentSite
    protected Site site;

    @HttpParameter(QUERY_PARAMETER)
    protected String queryString;

    @HttpParameter(SORT_PARAMETER)
    protected String sortString;

    @HttpParameter(PAGE_NUMBER_PARAMETER)
    protected long pageNumber;

    protected List<Object> queryTerms;
    protected Query<?> query;
    protected int limit;
    protected long count;
    protected long pageCount;
    protected PaginatedResult<?> result;
    protected List<Promo> spotlights;
    protected String path;

    protected Map<String, Set<String>> filterValues = new CompactMap<>();
    protected boolean isAllTypesSelected = true;
    protected Sort sort;

    protected static List<Object> parseQueryString(String queryString) {
        List<Object> queryTerms = new ArrayList<>();

        if (StringUtils.isBlank(queryString)) {
            return queryTerms;
        }
        Matcher matcher = QUOTATION_PATTERN.matcher(queryString);
        int beginIndex = matcher.regionStart();
        int endIndex;

        while (matcher.find()) {
            endIndex = matcher.start();
            if (endIndex > beginIndex) {
                queryTerms.addAll(tokenizeQueryString(queryString.substring(beginIndex, endIndex)));
            }
            Optional.of(matcher.group(1))
                .map(String::trim)
                .filter(q -> !q.isEmpty())
                .map(e -> e.contains(" ")
                    ? QueryPhrase.builder()
                    .phrase(e)
                    .proximity(1)
                    .build()
                    : e)
                .ifPresent(queryTerms::add);
            beginIndex = matcher.end();
        }
        endIndex = matcher.regionEnd();
        if (endIndex > beginIndex) {
            queryTerms.addAll(tokenizeQueryString(queryString.substring(beginIndex, endIndex)));
        }
        return queryTerms;
    }

    private static List<String> tokenizeQueryString(String queryString) {
        return Optional.ofNullable(queryString)
            .map(String::trim)
            .filter(q -> !q.isEmpty())
            .map(q -> Arrays.asList(TOKENIZATION_PATTERN.split(q)))
            .orElse(Collections.emptyList());
    }

    public String getQueryString() {
        return queryString;
    }

    public String getPath() {
        return path;
    }

    protected Map<String, Set<String>> getFilterValues() {
        return filterValues;
    }

    public boolean isAllTypesSelected() {
        return isAllTypesSelected;
    }

    protected Sort getSort() {
        return sort;
    }

    @Override
    protected void onCreate(ViewResponse response) {
        super.onCreate(response);

        queryTerms = parseQueryString(queryString);
        sort = model.getSorts().stream()
            .filter(s -> StringUtils.equals(s.getParameterValue(), sortString))
            .findFirst()
            .orElseGet(() -> model.getSorts().stream()
                .findFirst()
                .orElse(null));

        // get current spotlights

        spotlights = SearchSpotlight.getSpotlights(queryString, model.getDictionary(), model.getMaxSpotlights(), site);

        query = createQuery();

        HttpServletRequest request = PageContextFilter.Static.getRequest();

        model.getFilters().forEach(filter -> {
            String name = filter.getParameterName();
            String[] values = request.getParameterValues(name);

            if (values != null) {
                if (filter instanceof TypeFilter) {
                    isAllTypesSelected = false;
                }

                Set<String> valuesSet = ImmutableSet.copyOf(values);

                filter.updateQuery(this, query, valuesSet);
                filterValues.put(name, valuesSet);
            }
        });

        request.setAttribute(QUERY_ATTRIBUTE, query);

        limit = model.getResultsPerPage();
        count = query.count();

        pageCount = 1;

        // Page count is one unless there's more results than fit on a page
        if (count > limit) {
            // Otherwise, the number of pages needs to be divided onto the pages
            pageCount = count / limit;
            // If the results don't fit exactly on the pages (i.e., if there's a remainder), we need one additional page
            if (count % limit != 0) {
                ++pageCount;
            }
        }

        // Fix the page number if necessary.
        if (pageNumber < 1) {
            pageNumber = 1;

        } else if ((pageNumber - 1) * limit > count) {
            pageNumber = pageCount;
        }

        result = query.select((pageNumber - 1) * limit, limit);

        path = new UrlBuilder(request)
            .currentPath()
            .currentParameters()
            .toString();
    }

    protected Query<?> createQuery() {
        Query<?> query = Query.fromAll();
        query.and(SiteSearchableData.FIELD_INTERNAL_NAME_PREFIX + "hideFromSearchResults != true");

        if (!(ObjectUtils.isBlank(queryTerms) && Boolean.TRUE.equals(model.getShowAllContentOnEmptyQuery()))) {
            query.and("_any matches ?", queryTerms);
        } else {
            // force to solr anyway
            query.and("_any matches *");
        }
        query.and(Directory.Static.hasPathPredicate());

        model.getTypes().updateQuery(this, query);

        Site.ObjectModification siteData = model.as(Site.ObjectModification.class);
        Site owner = siteData.getOwner();
        Predicate siteItemsPredicate = owner != null ? owner.itemsPredicate() : null;
        Set<Site> consumers = siteData.getConsumers();

        if (consumers != null) {
            for (Site consumer : consumers) {
                siteItemsPredicate = CompoundPredicate.combine(
                    PredicateParser.OR_OPERATOR,
                    siteItemsPredicate,
                    consumer.itemsPredicate());
            }
        }

        if (siteItemsPredicate != null) {
            query.and(siteItemsPredicate);
        }

        if (sort != null) {
            sort.updateQuery(this, query);
        } else {
            // Default to relevancy sort
            new RelevanceSort().updateQuery(this, query);
        }

        if (!ObjectUtils.isBlank(spotlights)) {
            Collection<?> spotlightedAssets = Optional.ofNullable(spotlights
                .stream()
                .map(Promo::getContent)
                .filter(Optional::isPresent)
                .map(Optional::get))
                .orElse(Stream.empty())
                .collect(Collectors.toList());

            if (!spotlightedAssets.isEmpty()) {
                query.and(PredicateParser.Static.parse("_id != ?", spotlightedAssets));
            }
        }

        return query;
    }

    public Query<?> createQueryWithoutFilter(Filter filter) {
        Query<?> query = createQuery();

        model.getFilters().stream()
            .filter(f -> !f.equals(filter))
            .forEach(f -> {
                Set<String> values = filterValues.get(f.getParameterName());

                if (values != null) {
                    f.updateQuery(this, query, values);
                }
            });

        return query;
    }
}
