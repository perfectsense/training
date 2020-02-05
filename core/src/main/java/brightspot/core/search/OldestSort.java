package brightspot.core.search;

import com.psddev.cms.db.Content;
import com.psddev.dari.db.Query;

public class OldestSort extends Sort {

    @Override
    public String getDefaultName() {
        return "Oldest";
    }

    @Override
    public void updateQuery(SiteSearchViewModel search, Query<?> query) {
        query.sortAscending(Content.PUBLISH_DATE_FIELD);
    }
}
