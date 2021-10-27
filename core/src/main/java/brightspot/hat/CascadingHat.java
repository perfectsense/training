package brightspot.hat;

import brightspot.cascading.Cascading;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class CascadingHat extends Record implements Cascading<Hat> {

}
