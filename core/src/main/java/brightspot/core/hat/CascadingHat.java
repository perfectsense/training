package brightspot.core.hat;

import brightspot.core.cascading.Cascading;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class CascadingHat extends Record implements Cascading<Hat> {

}
