package bex.training.character;

import java.util.Collections;
import java.util.List;

import bex.training.countdown.Countdown;
import bex.training.movie.Movie;
import brightspot.core.page.AbstractContentPageViewModel;
import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.styleguide.core.list.ListView;
import com.psddev.styleguide.core.list.ListViewItemsField;
import com.psddev.styleguide.training.character.CharacterPageView;
import com.psddev.styleguide.training.character.CharacterPageViewCountdownsField;
import com.psddev.styleguide.training.character.CharacterPageViewFeaturedMoviesField;
import com.psddev.styleguide.training.character.CharacterPageViewImageField;

public class CharacterPageViewModel extends AbstractContentPageViewModel<Character> implements CharacterPageView, PageEntryView {

    // Character Page Support.

    /**
     * An implementation example for a basic model string field to JSON string field. This is the most common
     * type of implementation as it does not require any processing to accommodate the View contract. What comes
     * in from the model is what goes out to the View.
     *
     * @return A {@link CharSequence} representing the {@link Character}'s name. This is usually their actual birth name.
     */
    @Override
    public CharSequence getAlterEgo() {
        return model.getAlterEgo();
    }

    /**
     * An implementation example for a {@link com.psddev.cms.db.ToolUi.RichText} annotated string field to a Rich Text JSON
     * string field. The model data will need processing to properly represent HTML and {@link com.psddev.styleguide.core.enhancement.EnhancementView}s
     * that may be present from the RTE on the field.
     *
     * The method {@link RichTextUtils#buildHtml(Database, String, java.util.function.Function)} is used here and requires the following parameters:
     *
     * 1.) A {@link Database} (If you are not sure what to pass here, just send it the default Database as shown below).
     * 2.) The model data input.
     * 3.) A {@link java.util.function.Function} that takes in a View class and a string that will be invoked to create the final Views.
     *
     * @return The processed Views representing the {@link Character}'s full biography including HTML and enhancements.
     */
    @Override
    public CharSequence getBiography() {
        return RichTextUtils.buildInlineHtml(model.getState().getDatabase(), model.getFullBiography(), this::createView);
    }

    @Override
    public Iterable<? extends CharacterPageViewCountdownsField> getCountdowns() {
        return createViews(CharacterPageViewCountdownsField.class,
                Query.from(Countdown.class)
                        .where("villains = ?", model)
                        .select(0, 10)
                        .getItems());
    }

    @Override
    public Iterable<? extends CharacterPageViewFeaturedMoviesField> getFeaturedMovies() {

        // Query for movies where the character is featured.
        List<Movie> featuredMovies = Query.from(Movie.class).where("getFeaturedCharacters = ?", model).selectAll();

        if (!ObjectUtils.isBlank(featuredMovies)) {

            ListView.Builder builder = new ListView.Builder();
            builder.title("Featured Movies");

            for (Movie movie : featuredMovies) {
                builder.addToItems(createView(ListViewItemsField.class, movie));
            }

            return Collections.singleton(builder.build());
        }

        return null;
    }

    /**
     * An implementation example for a delegated View handler. In this example, an {@link brightspot.core.image.Image} already has
     * it's own {@link com.psddev.cms.view.ViewModel} and the logic on how to process an image model does not need to be duplicated.
     *
     * By returning the results of {@link com.psddev.cms.view.ViewModel#createViews(Class, Object)} after providing the View class
     * and model image data, it will fulfill the View contract by delegating the processing off to {@link brightspot.core.image.ImageViewModel}.
     *
     * @return The processed Views representing the {@link Character}'s image created by {@link brightspot.core.image.ImageViewModel}.
     */
    @Override
    public Iterable<? extends CharacterPageViewImageField> getImage() {
        return createViews(CharacterPageViewImageField.class, model.getImage());
    }

    /**
     * Another simple string example.
     *
     * @return The name of the {@link Character} unprocessed.
     */
    @Override
    public CharSequence getName() {
        return model.getName();
    }

    // Page Support.

    @Override
    public CharSequence getPageSubHeading() {
        return null;
    }
}
