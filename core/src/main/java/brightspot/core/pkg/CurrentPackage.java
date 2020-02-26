package brightspot.core.pkg;

import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Singleton;

/**
 * Singleton record to power {@link PackageDynamicQueryModifier} dropdown.
 */
public class CurrentPackage extends Record implements PackageOrCurrentPackage, Singleton {

    @Override
    protected void beforeSave() {
        super.beforeSave();
        as(Site.ObjectModification.class).setGlobal(true);
    }

    @Override
    public String getLabel() {
        // zero width space to affect sorting
        return '\u200B' + "Current Package";
    }
}
