package bex.training.movie;

import brightspot.core.page.AbstractContentPageViewModel;
import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.dari.db.Database;
import com.psddev.dari.util.StringUtils;
import com.psddev.styleguide.training.movie.MoviePageView;
import com.psddev.styleguide.training.movie.MoviePageViewCoverField;
import com.psddev.styleguide.training.movie.MoviePageViewFeaturedCharactersField;
import com.psddev.styleguide.training.movie.MoviePageViewPhaseField;
import com.psddev.styleguide.training.movie.MoviePageViewPlotField;
import com.psddev.styleguide.training.movie.MoviePageViewSummaryField;

public class MoviePageViewModel extends AbstractContentPageViewModel<Movie> implements MoviePageView, PageEntryView {

    // Movie Page Support.

    @Override
    public Iterable<? extends MoviePageViewCoverField> getCover() {
        return createViews(MoviePageViewCoverField.class, model.getCover());
    }

    @Override
    public Iterable<? extends MoviePageViewFeaturedCharactersField> getFeaturedCharacters() {
        return createViews(MoviePageViewFeaturedCharactersField.class, model.getFeaturedCharacters());
    }

    @Override
    public CharSequence getName() {
        return model.getName();
    }

    @Override
    public Iterable<? extends MoviePageViewPhaseField> getPhase() {
        return createViews(MoviePageViewPhaseField.class, model.getPhase());
    }

    @Override
    public Iterable<? extends MoviePageViewPlotField> getPlot() {
        if (!StringUtils.isBlank(model.getPlot())) {
            return RichTextUtils.buildHtml(Database.Static.getDefault(), model.getPlot(),
                    s -> createView(MoviePageViewPlotField.class, s));
        }

        return null;
    }

    @Override
    public Iterable<? extends MoviePageViewSummaryField> getSummary() {
        if (!StringUtils.isBlank(model.getSummary())) {
            return RichTextUtils.buildHtml(Database.Static.getDefault(), model.getSummary(),
                    s -> createView(MoviePageViewSummaryField.class, s));
        }

        return null;
    }

    // Page Support.

    @Override
    public CharSequence getPageSubHeading() {
        return null;
    }
}
