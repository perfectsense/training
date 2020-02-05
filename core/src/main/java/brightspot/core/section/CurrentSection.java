package brightspot.core.section;

import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Singleton;

/**
 * Singleton record to power {@link SectionDynamicQueryModifier} dropdown.
 */
public class CurrentSection extends Record implements SectionOrCurrentSection, Singleton {

    @Override
    protected void beforeSave() {
        super.beforeSave();
        as(Site.ObjectModification.class).setGlobal(true);
    }

    @Override
    public String getLabel() {
        // zero width space to affect sorting
        return '\u200B' + "Current Section";
    }
}
