package brightspot.core.search;

import java.util.Collection;

import brightspot.core.promo.Promotable;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("All")
public class AllTypes extends Types {

    private Collection<ObjectType> getTypes() {
        return Database.Static.getDefault()
            .getEnvironment()
            .getTypesByGroup(Promotable.class.getName());
    }

    @Override
    public void updateQuery(SiteSearchViewModel search, Query<?> query) {
        query.and("_type = ?", getTypes());
    }
}
