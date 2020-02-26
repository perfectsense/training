package brightspot.google.dfp;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class GoogleDfpAdClient extends Record {

    public abstract String getClientId();
}
