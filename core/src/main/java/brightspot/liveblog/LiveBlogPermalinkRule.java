package brightspot.liveblog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import brightspot.permalink.AbstractPermalinkRule;
import brightspot.urlslug.HasUrlSlugWithField;
import com.psddev.cms.db.Site;

public class LiveBlogPermalinkRule extends AbstractPermalinkRule {

    // --- AbstractPermalinkRule support ---

    @Override
    public String createPermalink(
        Site site,
        Object object) {

        // Validate type.
        if (!(object instanceof ILiveBlog)) {

            return null;
        }

        List<String> parts = new ArrayList<>();

        // Live Updates Path Prefix.
        parts.add("/live");

        // Live Blog Slug.
        Optional.of(object)
            .map(LiveBlog.class::cast)
            .map(HasUrlSlugWithField::getUrlSlug)
            .ifPresent(parts::add);

        return String.join("/", parts);
    }
}
