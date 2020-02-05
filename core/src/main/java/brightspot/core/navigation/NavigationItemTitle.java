package brightspot.core.navigation;

import com.psddev.dari.db.Recordable;

/**
 * Interface for classes that can represent a navigation item title (e.g., linked text vs. plain text)
 */
public interface NavigationItemTitle extends Recordable {

    String getTitleText();
}
