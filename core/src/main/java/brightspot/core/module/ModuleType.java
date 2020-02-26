package brightspot.core.module;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import brightspot.core.image.ImageOption;
import brightspot.core.listmodule.DynamicPin;
import brightspot.core.listmodule.ListModule;
import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class ModuleType extends Record implements BaseModuleType, DynamicPin {

    public static final String INTERNAL_NAME = "brightspot.core.module.ModuleType";

    public ImageOption getModuleTypeImage() {
        return null;
    }

    static ModuleType createDefault() {
        return DefaultImplementationSupplier.createDefault(ModuleType.class, ListModule.class);
    }

    /**
     * Returns the concrete Records being wrapped by the implementing ModuleType. The default implementation returns a
     * singleton set containing the ID of the current ModuleType.
     *
     * @return Concrete Record UUIDs wrapped by the ModuleType.
     */
    public Set<UUID> getModuleTypeContentIds() {
        return Collections.singleton(this.getState().getId());
    }
}
