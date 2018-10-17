package bex.training.character;

import brightspot.core.permalink.ExpressDefaultPermalinkRule;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;

public class VillainPermalinkRule extends Record implements ExpressDefaultPermalinkRule {

    @Override
    public String createPermalink(Site site, Object object) {

        if (!(object instanceof Villain)) {
            return null;
        }

        Villain villain = (Villain) object;
        String villainSlug = villain.asSluggableData().getSlug();
        return "/villain/" + villainSlug;
    }
}
