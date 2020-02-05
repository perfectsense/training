package brightspot.core.tool;

import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectIndex;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Substitution;

/**
 * The Modification data for SiteSingleton objects.
 */
@Recordable.FieldInternalNamePrefix(SiteSingletonData.DATA_FIELD_PREFIX)
public class SiteSingletonData extends Modification<SiteSingleton> {

    /**
     * SiteSingleton internal field name prefix.
     */
    public static final String DATA_FIELD_PREFIX = "bex.siteSingleton.";

    /**
     * Internal field name for the SiteSingleton unique key.
     */
    public static final String KEY_FIELD = DATA_FIELD_PREFIX + "key";

    @ToolUi.Hidden
    @Recordable.Indexed(unique = true)
    private String key;

    /**
     * @return the site unique key for this object.
     */
    public String getKey() {
        return key;
    }

    @Override
    public void beforeSave() {
        Site siteOwner = as(Site.ObjectModification.class).getOwner();
        Class nonSubstitutionClass = getNonSubstitutionSuperclass(getOriginalObject().getClass());
        key = nonSubstitutionClass.getName() + "/" + (siteOwner != null ? siteOwner.getId().toString() : "");
    }

    @Override
    protected boolean onDuplicate(ObjectIndex index) {

        if (index != null) {
            String field = index.getField();

            if (KEY_FIELD.equals(field)) {
                getState().addError(getState().getField(KEY_FIELD), "Only one "
                    + getState().getType().getLabel() + " can be saved per Site!");
            }
        }

        return false;
    }

    //Returns the closest superclass of currentClass which does not implement the Substitution interface
    private Class getNonSubstitutionSuperclass(Class currentClass) {
        //While the current class implements substitution
        while (Substitution.class.isAssignableFrom(currentClass)) {
            currentClass = currentClass.getSuperclass();
        }
        return currentClass;
    }

}
