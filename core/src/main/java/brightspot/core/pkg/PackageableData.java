package brightspot.core.pkg;

import java.util.Collections;
import java.util.Set;

import brightspot.core.classification.ClassificationDataItem;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("packageable.")
public class PackageableData extends Modification<Packageable> implements ClassificationDataItem {

    private static final String FIELD_PREFIX = "packageable.";

    public static final String PACKAGE_FIELD = FIELD_PREFIX + "pkg";

    @DisplayName("Package")
    @Indexed
    // @ToolUi.DropDown TODO: BSP-1515
    private Package pkg;

    public Package getPkg() {
        return pkg;
    }

    public void setPkg(Package pkg) {
        this.pkg = pkg;
    }

    @Override
    public Set<Recordable> getClassificationAttributes() {
        return Collections.singleton(getPkg());
    }
}
