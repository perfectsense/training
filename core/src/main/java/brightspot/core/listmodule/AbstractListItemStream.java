package brightspot.core.listmodule;

import java.util.List;

import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class AbstractListItemStream extends Record implements ItemStream {

    public abstract List<?> getItems();

    @Override
    public List<?> getItems(Site site, Object mainObject, long offset, int limit) {

        long toIndex = limit + offset;
        long count = getCount(site, mainObject);

        if (toIndex >= count) {
            toIndex = count;
        }

        return getItems().subList(Math.toIntExact(offset), Math.toIntExact(toIndex));
    }

    @Override
    public long getCount(Site site, Object mainObject) {
        return getItems().size();
    }

    @Override
    public boolean hasMoreThan(Site site, Object mainObject, long count) {
        return getCount(site, mainObject) > count;
    }
}
