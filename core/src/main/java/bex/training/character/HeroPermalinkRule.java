package bex.training.character;

import brightspot.core.permalink.AbstractPermalinkRule;
import com.psddev.cms.db.Site;

public class HeroPermalinkRule extends AbstractPermalinkRule {

    @Override
    public String createPermalink(Site site, Object object) {

        if (!(object instanceof Hero)) {
            return null;
        }

        Hero hero = (Hero) object;
        String heroSlug = hero.asSluggableData().getSlug();
        return "/hero/" + heroSlug;
    }
}
