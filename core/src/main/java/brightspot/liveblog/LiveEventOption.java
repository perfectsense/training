package brightspot.liveblog;

import java.time.Instant;

import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class LiveEventOption extends Record {

    public abstract boolean isLiveEvent(
        Site site,
        Instant lastUpdate,
        Instant now);
}
