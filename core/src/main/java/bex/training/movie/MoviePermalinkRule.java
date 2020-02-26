package bex.training.movie;

import java.util.Optional;

import brightspot.core.permalink.AbstractPermalinkRule;
import com.psddev.cms.db.Site;

public class MoviePermalinkRule extends AbstractPermalinkRule {

    @Override
    public String createPermalink(Site site, Object object) {

        if (!(object instanceof Movie)) {
            return null;
        }

        Movie movie = (Movie) object;

        // Phase path.
        String phasePath = Optional.ofNullable(movie.getPhase())
                .map(phase -> phase.createPermalink(site))
                .orElse(null);

        // Movie path.
        String movieSlug = movie.asSluggableData().getSlug();
        String moviePath = "/movie/" + movieSlug;

        return phasePath + moviePath;
    }
}
