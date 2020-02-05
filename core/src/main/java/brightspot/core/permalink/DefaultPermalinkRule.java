package brightspot.core.permalink;

import brightspot.core.slug.Sluggable;
import brightspot.core.slug.SluggableData;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;

/**
 * The default permalink rule for Express.  Constructs a normalized path string for this content from either the {@link
 * SluggableData#slug} or falls back to its {@link Recordable#getLabel()} implementation.
 */
public class DefaultPermalinkRule extends AbstractPermalinkRule {

    @Override
    public String createPermalink(Site site, Object object) {
        if (object instanceof Recordable) {
            Recordable recordable = (Recordable) object;

            String pathString = StringUtils.toNormalized(recordable.isInstantiableTo(Sluggable.class)
                ? recordable.as(SluggableData.class).getSlug()
                : recordable.getLabel());

            if (pathString != null) {
                return StringUtils.ensureStart(pathString, "/");
            }
        }

        return null;
    }

    // TODO: Localize
    @Override
    public String getLabel() {
        return "Default (Slug)";
    }
}
