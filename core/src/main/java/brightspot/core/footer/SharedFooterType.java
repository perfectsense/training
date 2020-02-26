package brightspot.core.footer;

import com.psddev.dari.db.Recordable;

public interface SharedFooterType extends Recordable {

    boolean isFooterModuleType();

    boolean isFooterModuleTypeOnly();
}
