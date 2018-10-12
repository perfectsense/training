package bex.training.character;

import brightspot.core.permalink.ExpressDefaultPermalinkRule;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;

public class HeroPermalinkRule extends Record implements ExpressDefaultPermalinkRule {

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
