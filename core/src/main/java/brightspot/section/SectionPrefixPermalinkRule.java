package brightspot.section;

import java.util.Optional;

import brightspot.permalink.AbstractPermalinkRule;
import brightspot.permalink.DefaultPermalinkRule;
import brightspot.permalink.Permalink;
import com.psddev.cms.db.Site;
import com.psddev.cms.ui.ToolLocalization;
import com.psddev.dari.db.State;
import org.apache.commons.lang3.StringUtils;

/**
 * Permalink rule that prefixes the normalized label for the content with a {@link Section} permalink path if
 * applicable.
 */
public class SectionPrefixPermalinkRule extends AbstractPermalinkRule {

    @Override
    public String createPermalink(Site site, Object object) {
        State state = State.getInstance(object);

        if (state == null || !state.isInstantiableTo(HasSection.class)) {
            return null;
        }

        String pathEnd = new DefaultPermalinkRule().createPermalink(site, object);

        if (StringUtils.isBlank(pathEnd)) {
            return null;
        }

        return Optional.ofNullable(state.as(HasSection.class).getSectionParent())
            .map(s -> Permalink.getRelativePermalink(site, s))
            .map(p -> StringUtils.prependIfMissing(p, "/"))
            .map(p -> StringUtils.appendIfMissing(p, "/"))
            .orElse("/")
            + StringUtils.removeStart(pathEnd, "/");
    }

    @Override
    public String getLabel() {
        return ToolLocalization.text(SectionPrefixPermalinkRule.class, "label", "Section Prefix");
    }
}
