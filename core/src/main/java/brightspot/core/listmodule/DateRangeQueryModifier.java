package brightspot.core.listmodule;

import brightspot.core.imageitemstream.DynamicImageItemStream;
import brightspot.core.timedcontentitemstream.DynamicTimedContentItemStream;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;
import org.joda.time.DateTime;

@Modification.Classes({
    DynamicItemStream.class,
    DynamicImageItemStream.class,
    DynamicTimedContentItemStream.class,
    DateRangeQueryModifiable.class })
public class DateRangeQueryModifier extends Modification<Object> implements DynamicQueryModifier {

    @ToolUi.NoteHtml("Limit to content published within this number of days")
    private Integer timePeriod;

    @Override
    public void updateQuery(Site site, Object mainObject, Query<?> query) {

        if (timePeriod != null && timePeriod > 0) {

            DateTime then = DateTime.now().minusDays(timePeriod);

            query.where(Content.PUBLISH_DATE_FIELD + " >= ?", then.toDate());
        }
    }

    @Override
    public String createLabel() {
        if (timePeriod != null && timePeriod > 0) {
            return "Last " + timePeriod + " days";
        }
        return null;
    }
}
