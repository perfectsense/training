package brightspot.footer;

import brightspot.cascading.Cascading;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class CascadingFooter extends Record implements Cascading<Footer> {

}
