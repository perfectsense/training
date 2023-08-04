package brightspot.image;

import brightspot.cascading.Cascading;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class CascadingWebImage extends Record implements Cascading<WebImage> {
}
