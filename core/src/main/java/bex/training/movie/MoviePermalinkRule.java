package bex.training.movie;

import brightspot.core.permalink.ExpressDefaultPermalinkRule;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Record;

import java.util.Optional;

public class MoviePermalinkRule extends Record implements ExpressDefaultPermalinkRule {

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
