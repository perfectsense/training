package brightspot.core.navigation;

import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("None")
public class CascadingNavigationNone extends CascadingNavigation {

    @Override
    public Navigation get() {
        return null;
    }
}
