package brightspot.core.feature;

import java.util.HashSet;
import java.util.Set;

import brightspot.core.page.Page;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;

/**
 * Globally adds a "Disabled Features" dropdown of concrete {@link Feature} implementations to content.
 */
@Recordable.FieldInternalNamePrefix("features.")
public class FeatureSettings extends Modification<Page> {

    @ToolUi.Tab("Overrides")
    @ToolUi.Cluster("Feature Overrides")
    @ToolUi.DropDown
    @Where("groups = brightspot.core.feature.Feature and isAbstract != true")
    private Set<ObjectType> disabledFeatures;

    public Set<ObjectType> getDisabledFeatures() {
        if (disabledFeatures == null) {
            disabledFeatures = new HashSet<>();
        }

        return disabledFeatures;
    }

    public void setDisabledFeatures(Set<ObjectType> disabledFeatures) {
        this.disabledFeatures = disabledFeatures;
    }
}
