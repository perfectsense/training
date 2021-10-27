package brightspot.page;

import java.util.HashSet;
import java.util.Set;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("cascading.")
public class TypeSpecificPageElementsModification extends Modification<TypeSpecificCascadingPageElements> {

    @ToolUi.Cluster("Type Specific Overrides")
    @ToolUi.Tab("Overrides")
    private Set<TypeSpecificPageElements> typeSpecificOverrides;

    public Set<TypeSpecificPageElements> getTypeSpecificOverrides() {
        if (typeSpecificOverrides == null) {
            typeSpecificOverrides = new HashSet<>();
        }
        return typeSpecificOverrides;
    }

    public void setTypeSpecificOverrides(Set<TypeSpecificPageElements> typeSpecificOverrides) {
        this.typeSpecificOverrides = typeSpecificOverrides;
    }
}
