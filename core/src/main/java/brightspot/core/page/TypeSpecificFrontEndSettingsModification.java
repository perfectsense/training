package brightspot.core.page;

import java.util.HashSet;
import java.util.Set;

import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;

public class TypeSpecificFrontEndSettingsModification extends Modification<SiteSettings> {

    @ToolUi.Cluster("Type Specific Overrides")
    @ToolUi.Tab("Page Defaults")
    @ToolUi.DisplayLast
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
