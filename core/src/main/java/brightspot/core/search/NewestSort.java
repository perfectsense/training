package brightspot.core.search;

import com.psddev.cms.db.Content;
import com.psddev.dari.db.Query;

public class NewestSort extends Sort {

    @Override
    public String getDefaultName() {
        return "Newest";
    }

    @Override
    public void updateQuery(SiteSearchViewModel search, Query<?> query) {
        query.sortDescending(Content.PUBLISH_DATE_FIELD);
    }
}
