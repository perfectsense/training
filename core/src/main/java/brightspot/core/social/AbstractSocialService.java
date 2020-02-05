package brightspot.core.social;

import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Singleton;

public abstract class AbstractSocialService extends Record implements SocialService, Singleton {

    AbstractSocialService() {
        this.as(Site.ObjectModification.class).setGlobal(true);
    }

    @Override
    public String getLabel() {
        return getDisplayName();
    }
}
