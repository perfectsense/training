package brightspot.event;

import brightspot.dqm.DynamicQueryModifier;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;

public class LiveContentDynamicQueryModifier extends
    Modification<LiveContentDynamicQueryModifiableInterface>
    implements DynamicQueryModifier {

    private boolean liveContentOnly;

    @Override
    public void updateQuery(Site site, Object mainContent, Query<?> query) {
        if (liveContentOnly) {
            query.where("brightspot.event.LiveContentData/isLive = true");
        }
    }

    @Override
    public String createLabel() {
        return null;
    }
}
