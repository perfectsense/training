package brightspot.core.navigation;

import brightspot.core.cascading.Cascading;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class CascadingNavigation extends Record implements Cascading<Navigation> {

}
