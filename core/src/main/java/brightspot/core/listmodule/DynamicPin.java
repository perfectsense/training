package brightspot.core.listmodule;

import brightspot.core.promo.Promo;
import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.dari.db.Recordable;

public interface DynamicPin extends Recordable {

    static DynamicPin createDefault() {
        return DefaultImplementationSupplier.createDefault(DynamicPin.class, Promo.class);
    }
}
