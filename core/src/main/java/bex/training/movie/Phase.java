package bex.training.movie;

import brightspot.core.section.Section;
import brightspot.core.slug.Sluggable;
import com.psddev.dari.util.StringUtils;

/**
 * A {@link Phase} is a grouping of movies in the MCU.
 */
public class Phase extends Section implements Sluggable {

    // Overrides.

    @Override
    public String getSluggableSlugFallback() {
        return StringUtils.toNormalized(getDisplayName());
    }
}
