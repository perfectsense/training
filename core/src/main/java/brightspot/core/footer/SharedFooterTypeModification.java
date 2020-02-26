package brightspot.core.footer;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;

public class SharedFooterTypeModification extends Modification<SharedFooterType> {

    @Indexed
    @ToolUi.Hidden
    public boolean isFooterModuleType() {
        return getOriginalObject().isFooterModuleType();
    }

    @Indexed
    @ToolUi.Hidden
    public boolean isFooterModuleTypeOnly() {
        return getOriginalObject().isFooterModuleTypeOnly();
    }
}
