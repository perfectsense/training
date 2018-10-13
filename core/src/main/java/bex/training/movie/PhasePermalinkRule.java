package bex.training.movie;

import brightspot.core.permalink.ExpressDefaultPermalinkRule;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;

public class PhasePermalinkRule extends Record implements ExpressDefaultPermalinkRule {

    @Override
    public String createPermalink(Site site, Object object) {

        if (!(object instanceof Phase)) {
            return null;
        }

        Phase phase = (Phase) object;
        String phaseSlug = phase.asSluggableData().getSlug();
        return "/phase/" + phaseSlug;
    }
}
