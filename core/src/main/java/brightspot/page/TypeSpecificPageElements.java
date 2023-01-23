package brightspot.page;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.cascading.CascadingPageElements;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class TypeSpecificPageElements extends Record implements CascadingPageElements {

    @ToolUi.Tab("Overrides")
    @Where("groups = " + Page.INTERNAL_NAME + " and isAbstract = false")
    @CollectionMinimum(1)
    public Set<ObjectType> types;

    public Set<ObjectType> getTypes() {
        if (types == null) {
            types = new HashSet<>();
        }
        return types;
    }

    public void setTypes(Set<ObjectType> types) {
        this.types = types;
    }

    @Override
    public String getLabel() {
        return getTypes()
            .stream()
            .map(ObjectType::getLabel)
            .collect(Collectors.joining(", "));
    }
}
