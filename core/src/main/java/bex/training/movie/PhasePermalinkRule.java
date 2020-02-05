package bex.training.movie;

import brightspot.core.permalink.AbstractPermalinkRule;
import com.psddev.cms.db.Site;

public class PhasePermalinkRule extends AbstractPermalinkRule {

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
