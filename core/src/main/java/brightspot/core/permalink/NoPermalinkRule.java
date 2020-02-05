package brightspot.core.permalink;

import com.psddev.cms.db.Site;

/**
 * Rule to force an empty permalink
 **/
public class NoPermalinkRule extends AbstractPermalinkRule {

    @Override
    public String createPermalink(Site site, Object object) {
        return null;
    }

    @Override
    public String getLabel() {
        // TODO: localize
        return "No Permalink";
    }
}
