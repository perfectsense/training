package brightspot.core.background;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class Background extends Record {

    public abstract String getCssValue();
}
