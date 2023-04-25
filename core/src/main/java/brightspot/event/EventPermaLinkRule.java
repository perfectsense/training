package brightspot.event;

import java.text.SimpleDateFormat;

import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.DefaultPermalinkRule;
import com.psddev.cms.db.Site;

public class EventPermaLinkRule extends AbstractPermalinkRule {

    @Override
    public String createPermalink(Site site, Object object) {
        if (!(object instanceof Event)) {
            return null;
        }
        String pathStart = new DefaultPermalinkRule().createPermalink(site, object);
        Event event = (Event) object;

        if (event.getStartDate() != null) {
            String startDateFormatted = new SimpleDateFormat("yyyy-MM-dd").format(event.getStartDate());
            pathStart = pathStart + "-" + startDateFormatted;
        }
        return pathStart;
    }
}