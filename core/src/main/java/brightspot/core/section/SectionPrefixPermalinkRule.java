package brightspot.core.section;

import java.util.Optional;

import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.permalink.DefaultPermalinkRule;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.State;
import com.psddev.dari.util.StringUtils;

/**
 * Permalink rule that prefixes the normalized label for the content with a {@link Section} permalink path if
 * applicable.
 */
public class SectionPrefixPermalinkRule extends AbstractPermalinkRule {

    @Override
    public String createPermalink(Site site, Object object) {
        State state = State.getInstance(object);

        if (state == null) {
            return null;
        }

        SectionableData sectionableData = state.as(SectionableData.class);

        if (sectionableData != null) {
            String pathEnd = new DefaultPermalinkRule().createPermalink(site, object);

            if (StringUtils.isBlank(pathEnd)) {
                return null;
            }

            Section section = sectionableData.getSection();

            return StringUtils.ensureStart(Optional.ofNullable(section)
                .map(s -> s.createPermalink(site))
                .orElse(""), "/") + pathEnd;
        }

        return null;
    }

    @Override
    public String getLabel() {
        // TODO: localize
        return "Section Prefix";
    }
}
