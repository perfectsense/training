package brightspot.core.search;

import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class Types extends Record {

    public abstract void updateQuery(SiteSearchViewModel search, Query<?> query);
}
