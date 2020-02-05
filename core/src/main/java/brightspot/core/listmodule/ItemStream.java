package brightspot.core.listmodule;

import java.util.List;

import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;

public interface ItemStream extends Recordable {

    List<?> getItems(Site site, Object mainObject, long offset, int limit);

    long getCount(Site site, Object mainObject);

    boolean hasMoreThan(Site site, Object mainObject, long count);

    int getItemsPerPage(Site site, Object mainObject);

    default String getTitlePlaceholder() {
        return null;
    }
}
