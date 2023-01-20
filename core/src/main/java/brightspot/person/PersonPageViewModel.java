package brightspot.person;

import brightspot.page.AbstractContentPageViewModel;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.jsonld.JsonLdType;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import com.psddev.styleguide.person.PersonPageView;
import com.psddev.styleguide.person.PersonPageViewAffiliationField;
import com.psddev.styleguide.person.PersonPageViewFirstNameField;
import com.psddev.styleguide.person.PersonPageViewFullBiographyField;
import com.psddev.styleguide.person.PersonPageViewImageField;
import com.psddev.styleguide.person.PersonPageViewJobTitleField;
import com.psddev.styleguide.person.PersonPageViewLastNameField;
import com.psddev.styleguide.person.PersonPageViewLinksField;
import com.psddev.styleguide.person.PersonPageViewNameField;
import com.psddev.styleguide.person.PersonPageViewPersonSocialLinksField;
import com.psddev.styleguide.person.PersonPageViewShortBiographyField;

@JsonLdType("WebPage")
public class PersonPageViewModel extends AbstractContentPageViewModel<PersonPage> implements PersonPageView,
        PageEntryView {

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return null;
    }

    @Override
    public Iterable<? extends PersonPageViewAffiliationField> getAffiliation() {
        return RichTextUtils.buildInlineHtml(
                model,
                PersonPage::getAffiliation,
                e -> createView(PersonPageViewAffiliationField.class, e));
    }

    @Override
    public CharSequence getEmail() {
        return model.getEmail();
    }

    @Override
    public Iterable<? extends PersonPageViewFirstNameField> getFirstName() {
        return RichTextUtils.buildInlineHtml(
                model,
                PersonPage::getFirstName,
                e -> createView(PersonPageViewFirstNameField.class, e));
    }

    @Override
    public Iterable<? extends PersonPageViewFullBiographyField> getFullBiography() {
        return RichTextUtils.buildHtml(
                model,
                PersonPage::getFullBiography,
                e -> createView(PersonPageViewFullBiographyField.class, e));
    }

    @Override
    public Iterable<? extends PersonPageViewImageField> getImage() {
        return createViews(PersonPageViewImageField.class, model.getImage());
    }

    @Override
    public Iterable<? extends PersonPageViewJobTitleField> getJobTitle() {
        return RichTextUtils.buildInlineHtml(
                model,
                PersonPage::getTitle,
                e -> createView(PersonPageViewJobTitleField.class, e));
    }

    @Override
    public Iterable<? extends PersonPageViewLastNameField> getLastName() {
        return RichTextUtils.buildInlineHtml(
                model,
                PersonPage::getLastName,
                e -> createView(PersonPageViewLastNameField.class, e));
    }

    @Override
    public Iterable<? extends PersonPageViewLinksField> getLinks() {
        return null;
    }

    @Override
    public Iterable<? extends PersonPageViewNameField> getName() {
        return RichTextUtils.buildInlineHtml(
                model,
                PersonPage::getName,
                e -> createView(PersonPageViewNameField.class, e));
    }

    @Override
    public Iterable<? extends PersonPageViewPersonSocialLinksField> getPersonSocialLinks() {
        return createViews(PersonPageViewPersonSocialLinksField.class, model);
    }

    @Override
    public Iterable<? extends PersonPageViewShortBiographyField> getShortBiography() {
        return RichTextUtils.buildHtml(model, PersonPage::getShortBiography, e -> createView(PersonPageViewShortBiographyField.class, e));
    }

}
