package bex.training.character;

import brightspot.core.permalink.AbstractPermalinkRule;
import com.psddev.cms.db.Site;

public class VillainPermalinkRule extends AbstractPermalinkRule {

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
