package brightspot.page;

import java.util.HashSet;
import java.util.Set;

import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("cascading.")
public class TypeSpecificOverridesSettings extends Modification<SiteSettings> {

    @ToolUi.Cluster("Type Specific Overrides")
    @ToolUi.Tab("Front-End")
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
