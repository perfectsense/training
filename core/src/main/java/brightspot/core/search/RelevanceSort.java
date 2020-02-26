package brightspot.core.search;

import com.psddev.cms.db.Content;
import com.psddev.dari.db.Query;

public class RelevanceSort extends Sort {

    @Override
    public String getDefaultName() {
        return "Relevance";
    }

    @Override
    public void updateQuery(SiteSearchViewModel search, Query<?> query) {
        String queryString = search.getQueryString();
        query.sortRelevant(100.0, "_label = ?", queryString);
        query.sortRelevant(50.0, "_any matchesAll ?", queryString);
        query.sortRelevant(10.0, "_label matches ?", queryString);

        // boost newer slightly
        query.sortNewest(5.0, Content.PUBLISH_DATE_FIELD);
    }
}
