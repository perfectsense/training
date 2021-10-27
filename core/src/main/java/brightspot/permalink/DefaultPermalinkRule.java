package brightspot.permalink;

import brightspot.seo.Seo;
import brightspot.urlslug.HasUrlSlug;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.Utils;
import org.apache.commons.lang3.StringUtils;

/**
 * The default permalink rule, which creates the path using {@link HasUrlSlug#getUrlSlug()} or
 * {@link Seo#getSeoTitle()}.
 */
public class DefaultPermalinkRule extends AbstractPermalinkRule {

    @Override
    public String createPermalink(Site site, Object object) {
        if (!(object instanceof Recordable)) {
            return null;
        }

        Recordable recordable = (Recordable) object;

        String path = null;

        if (recordable.isInstantiableTo(HasUrlSlug.class)) {
            path = recordable.as(HasUrlSlug.class).getUrlSlug();

        } else if (recordable.isInstantiableTo(Seo.class)) {
            path = recordable.as(Seo.class).getSeoTitle();
        }

        if (StringUtils.isBlank(path)) {
            return null;
        }

        return StringUtils.prependIfMissing(Utils.toNormalized(path), "/");
    }

    @Override
    public String getLabel() {
        return "Default (Slug or SEO Title)";
    }
}
