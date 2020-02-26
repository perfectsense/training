package brightspot.core.containermodule;

import java.util.Set;
import java.util.UUID;

import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.dari.db.Recordable;

/**
 * Marker interface for the types of options used in a {@link ContainerModule}.
 */
public interface ContainerColumnOption extends Recordable {

    static ContainerColumnOption createDefault() {
        return DefaultImplementationSupplier.createDefault(ContainerColumnOption.class, TwoColumnContainer.class);
    }

    /**
     * Returns the IDs of the Records contained in the columns on the implementing {@link ContainerColumnOption}.
     *
     * @return Concrete Record IDs in columns
     */
    Set<UUID> getContainerContentIds();
}
