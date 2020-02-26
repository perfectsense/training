package brightspot.core.listmodule;

import brightspot.core.module.ModuleType;
import brightspot.core.tool.DefaultImplementationSupplier;

public interface ListModuleItemStream extends ItemStream {

    static ListModuleItemStream createDefault() {
        return DefaultImplementationSupplier.createDefault(ListModuleItemStream.class, SimpleItemStream.class);
    }

    static ListModuleItemStream createDynamic() {
        return new DynamicItemStream();
    }

    default ModuleType getNoResultsModule() {
        return null;
    }
}
