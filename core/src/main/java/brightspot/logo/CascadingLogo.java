package brightspot.logo;

import brightspot.cascading.Cascading;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class CascadingLogo extends Record implements Cascading<Logo> {

}
