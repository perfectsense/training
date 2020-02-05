package brightspot.core.logo;

import brightspot.core.footer.SharedFooterType;
import com.psddev.dari.db.Record;

public abstract class Logo extends Record implements SharedFooterType {

    @Override
    public boolean isFooterModuleType() {
        return true;
    }

    @Override
    public boolean isFooterModuleTypeOnly() {
        return true;
    }
}
