package brightspot.core.requestextras.headelement;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * A script or style to be added to a matching request.
 */
@Recordable.Embedded
public abstract class HeadElement extends Record {

    @Required
    private String internalName;

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }
}
