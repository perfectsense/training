package brightspot.core.listmodule;

import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

public interface DynamicQuerySort extends Recordable {

    void updateQuery(Site site, Object mainObject, Query<?> query);

    static DynamicQuerySort createDefault() {
        return DefaultImplementationSupplier.createDefault(DynamicQuerySort.class, Newest.class);
    }
}
