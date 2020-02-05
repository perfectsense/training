package brightspot.core.search;

import com.psddev.dari.db.ObjectType;

public class TypeFilter extends FieldFilter {

    @Override
    public String getDefaultHeading() {
        return "Type";
    }

    @Override
    public String getField() {
        return "_type";
    }

    @Override
    public String getItemLabel(Object object) {
        return object instanceof ObjectType ? ((ObjectType) object).getDisplayName() : null;
    }
}
