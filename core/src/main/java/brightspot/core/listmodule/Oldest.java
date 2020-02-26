package brightspot.core.listmodule;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class Oldest extends Record implements DynamicQuerySort {

    @Override
    public void updateQuery(Site site, Object mainObject, Query<?> query) {
        query.sortAscending(Content.PUBLISH_DATE_FIELD);
    }
}
