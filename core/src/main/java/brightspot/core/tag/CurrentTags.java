package brightspot.core.tag;

import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Singleton;

/**
 * Singleton record to power {@link TagDynamicQueryModifier} dropdown.
 */
public class CurrentTags extends Record implements TagOrCurrentTags, Singleton {

    @Override
    protected void beforeSave() {
        super.beforeSave();
        as(Site.ObjectModification.class).setGlobal(true);
    }

    @Override
    public String getLabel() {
        // zero width space to affect sorting
        return '\u200B' + "Current Tag(s)";
    }
}
