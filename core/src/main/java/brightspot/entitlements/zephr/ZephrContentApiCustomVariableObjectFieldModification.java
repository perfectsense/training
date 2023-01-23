package brightspot.entitlements.zephr;

import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.Recordable;

/**
 * ObjectField metadata for {@link ZephrContentApiCustomVariable} annotations.
 */
@Recordable.FieldInternalNamePrefix(ZephrContentApiCustomVariableObjectFieldModification.PREFIX)
public class ZephrContentApiCustomVariableObjectFieldModification extends Modification<ObjectField> {

    static final String PREFIX = "zephr.";

    private Boolean zephrCustomVariable;

    public boolean isZephrCustomVariable() {
        return Boolean.TRUE.equals(zephrCustomVariable);
    }

    public void setZephrCustomVariable(Boolean zephrCustomVariable) {
        this.zephrCustomVariable = Boolean.TRUE.equals(zephrCustomVariable) ? true : null;
    }
}
