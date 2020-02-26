package brightspot.core.navigation;

import java.util.List;

import brightspot.core.footer.SharedFooterType;
import com.psddev.dari.db.Recordable;

/**
 * Marker interface for navigation bars.
 */
public interface Navigation
    extends Recordable,
    SharedFooterType {

    List<NavigationItem> getItems();

    @Override
    default boolean isFooterModuleType() {
        return true;
    }

    @Override
    default boolean isFooterModuleTypeOnly() {
        return true;
    }
}
