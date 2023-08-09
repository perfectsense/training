package brightspot.search.boost;

import java.util.List;

import brightspot.author.HasAuthorsData;
import brightspot.author.PersonAuthor;
import brightspot.person.PersonPage;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Standard")
public class StandardBoostConfiguration extends BoostConfiguration {

    @Override
    public void updateQuery(Site site, Object mainContent, Query<?> query) {
        for (Boost boost : getBoosts()) {
            boost.updateQuery(site, mainContent, query);
        }
    }

    @Override
    public List<Boost> getBoosts() {
        // Author Names Boosts
        ExactMatchBoost authorNamesExact = new ExactMatchBoost();
        authorNamesExact.setCurrentWeight(3.0);
        authorNamesExact.setIndex(HasAuthorsData.class.getName() + "/" + HasAuthorsData.AUTHOR_NAMES_FIELD);

        PartialMatchBoost authorNamesPartial = new PartialMatchBoost();
        authorNamesPartial.setCurrentWeight(9.0);
        authorNamesPartial.setIndex(HasAuthorsData.class.getName() + "/" + HasAuthorsData.AUTHOR_NAMES_FIELD);

        // Title Boosts
        ExactMatchBoost titleExact = new ExactMatchBoost();
        titleExact.setCurrentWeight(1.0);
        titleExact.setIndex(HasSiteSearchBoostIndexesData.class.getName() + "/"
            + HasSiteSearchBoostIndexesData.SEARCH_BOOST_TITLE_FIELD);

        PartialMatchBoost titlePartial = new PartialMatchBoost();
        titlePartial.setCurrentWeight(10.0);
        titlePartial.setIndex(HasSiteSearchBoostIndexesData.class.getName() + "/"
            + HasSiteSearchBoostIndexesData.SEARCH_BOOST_TITLE_FIELD);

        // Description Boost
        PartialMatchBoost descriptionPartial = new PartialMatchBoost();
        descriptionPartial.setCurrentWeight(7.0);
        descriptionPartial.setIndex(HasSiteSearchBoostIndexesData.class.getName() + "/"
            + HasSiteSearchBoostIndexesData.SEARCH_BOOST_DESCRIPTION_FIELD);

        // Person Boosts
        ExactMatchBoost personFirstNameExact = new ExactMatchBoost();
        personFirstNameExact.setCurrentWeight(4.0);
        personFirstNameExact.setIndex(PersonPage.class.getName() + "/firstName");

        PartialMatchBoost personFirstNamePartial = new PartialMatchBoost();
        personFirstNamePartial.setCurrentWeight(4.0);
        personFirstNamePartial.setIndex(PersonPage.class.getName() + "/firstName");

        ExactMatchBoost personLastNameExact = new ExactMatchBoost();
        personLastNameExact.setCurrentWeight(8.0);
        personLastNameExact.setIndex(PersonPage.class.getName() + "/lastName");

        PartialMatchBoost personLastNamePartial = new PartialMatchBoost();
        personLastNamePartial.setCurrentWeight(4.0);
        personLastNamePartial.setIndex(PersonPage.class.getName() + "/lastName");

        // Author Boosts
        ExactMatchBoost authorFirstNameExact = new ExactMatchBoost();
        authorFirstNameExact.setCurrentWeight(4.0);
        authorFirstNameExact.setIndex(PersonAuthor.class.getName() + "/firstName");

        PartialMatchBoost authorFirstNamePartial = new PartialMatchBoost();
        authorFirstNamePartial.setCurrentWeight(4.0);
        authorFirstNamePartial.setIndex(PersonAuthor.class.getName() + "/firstName");

        ExactMatchBoost authorLastNameExact = new ExactMatchBoost();
        authorLastNameExact.setCurrentWeight(8.0);
        authorLastNameExact.setIndex(PersonAuthor.class.getName() + "/lastName");

        PartialMatchBoost authorLastNamePartial = new PartialMatchBoost();
        authorLastNamePartial.setCurrentWeight(4.0);
        authorLastNamePartial.setIndex(PersonAuthor.class.getName() + "/lastName");

        // Publish Date
        NewestBoost publishDate = new NewestBoost();
        publishDate.setCurrentWeight(1.0);
        publishDate.setIndex("cms.content.publishDate");

        return ImmutableList.of(
            authorNamesExact,
            authorNamesPartial,
            titleExact,
            titlePartial,
            descriptionPartial,
            personFirstNameExact,
            personFirstNamePartial,
            personLastNameExact,
            personLastNamePartial,
            authorFirstNameExact,
            authorFirstNamePartial,
            authorLastNameExact,
            authorLastNamePartial,
            publishDate
        );
    }
}
