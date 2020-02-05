package brightspot.core.search;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import brightspot.core.link.Linkable;
import brightspot.core.navigation.NavigationSearch;
import brightspot.core.page.Page;
import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.tool.DirectoryItemUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.terms.Dictionary;

@Seo.TitleFields("getSeoTitle")
@Seo.DescriptionFields("getSeoDescription")
public class SiteSearch extends Content implements
    Directory.Item,
    Linkable,
    NavigationSearch,
    Page {

    private static final int DEFAULT_RESULTS_PER_PAGE = 10;

    @Required
    private String title;

    @ToolUi.Placeholder("" + DEFAULT_RESULTS_PER_PAGE)
    private Integer resultsPerPage;

    @Required
    private brightspot.core.search.Types types = new AllTypes();

    private List<Filter> filters;
    private List<Sort> sorts;

    @ToolUi.DropDown
    @ToolUi.Cluster("Search Spotlight")
    private Dictionary dictionary;

    @ToolUi.Placeholder("Unlimited")
    @ToolUi.Cluster("Search Spotlight")
    private Integer maxSpotlights;

    @ToolUi.Note("When no search terms are provided, display all content instead?")
    private Boolean showAllContentOnEmptyQuery;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResultsPerPage() {
        return resultsPerPage != null && resultsPerPage > 0
            ? resultsPerPage
            : DEFAULT_RESULTS_PER_PAGE;
    }

    public void setResultsPerPage(Integer resultsPerPage) {
        this.resultsPerPage = resultsPerPage;
    }

    public brightspot.core.search.Types getTypes() {
        return types;
    }

    public void setTypes(brightspot.core.search.Types types) {
        this.types = types;
    }

    public List<Filter> getFilters() {
        if (filters == null) {
            filters = new ArrayList<>();
        }
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<Sort> getSorts() {
        if (sorts == null) {
            sorts = new ArrayList<>();
        }
        return sorts;
    }

    public void setSorts(List<Sort> sorts) {
        this.sorts = sorts;
    }

    @Override
    public CharSequence getSearchAction(Site site) {
        return DirectoryItemUtils.getCanonicalUrl(site, this);
    }

    @Override
    protected void beforeCommit() {
        IntStream.range(0, getFilters().size())
            .forEach(i -> getFilters().get(i).setParameterName("f" + i));

        IntStream.range(0, getSorts().size())
            .forEach(i -> getSorts().get(i).setParameterValue(String.valueOf(i)));
    }

    @Override
    public String getLinkableText() {
        return getTitle();
    }

    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoTitle() {
        return getTitle();
    }

    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoDescription() {
        return null;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public Integer getMaxSpotlights() {
        return maxSpotlights;
    }

    public void setMaxSpotlights(Integer maxSpotlights) {
        this.maxSpotlights = maxSpotlights;
    }

    public Boolean getShowAllContentOnEmptyQuery() {
        return showAllContentOnEmptyQuery;
    }

    public void setShowAllContentOnEmptyQuery(Boolean showAllContentOnEmptyQuery) {
        this.showAllContentOnEmptyQuery = showAllContentOnEmptyQuery;
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, (s) -> "/search");
    }
}
