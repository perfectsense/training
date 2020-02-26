package brightspot.core.search;

import java.util.LinkedHashSet;
import java.util.Set;

import brightspot.core.promo.Promotable;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Select")
public class SelectTypes extends Types {

    @Required
    @ToolUi.DropDown
    @ToolUi.Unlabeled
    @Where("groups = '" + Promotable.INTERNAL_NAME + "' and isAbstract != true and isEmbedded != true")
    private Set<ObjectType> types;

    public Set<ObjectType> getTypes() {
        if (types == null) {
            types = new LinkedHashSet<>();
        }
        return types;
    }

    public void setTypes(Set<ObjectType> types) {
        this.types = types;
    }

    @Override
    public void updateQuery(SiteSearchViewModel search, Query<?> query) {
        query.and("_type = ?", getTypes());
    }
}
