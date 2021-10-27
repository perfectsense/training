package brightspot.author;

import java.util.Optional;

import brightspot.page.AbstractContentPageViewModel;
import brightspot.seo.PersonSchemaView;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.jsonld.JsonLd;
import com.psddev.cms.view.jsonld.JsonLdType;
import com.psddev.styleguide.RawHtml;
import com.psddev.styleguide.author.AuthorPageView;
import com.psddev.styleguide.author.AuthorPageViewAffiliationField;
import com.psddev.styleguide.author.AuthorPageViewAuthorSocialLinksField;
import com.psddev.styleguide.author.AuthorPageViewFirstNameField;
import com.psddev.styleguide.author.AuthorPageViewFullBiographyField;
import com.psddev.styleguide.author.AuthorPageViewImageField;
import com.psddev.styleguide.author.AuthorPageViewJobTitleField;
import com.psddev.styleguide.author.AuthorPageViewLastNameField;
import com.psddev.styleguide.author.AuthorPageViewLinksField;
import com.psddev.styleguide.author.AuthorPageViewNameField;
import com.psddev.styleguide.author.AuthorPageViewShortBiographyField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;

@JsonLdType("WebPage")
public class PersonAuthorPageViewModel extends AbstractContentPageViewModel<PersonAuthor> implements AuthorPageView, PageEntryView {

    @Override
    public Iterable<? extends AuthorPageViewAffiliationField> getAffiliation() {
        return RichTextUtils.buildInlineHtml(
                model,
                PersonAuthor::getAffiliation,
                e -> createView(AuthorPageViewAffiliationField.class, e));
    }

    @Override
    public Iterable<? extends AuthorPageViewAuthorSocialLinksField> getAuthorSocialLinks() {
        return createViews(AuthorPageViewAuthorSocialLinksField.class, model);
    }

    @Override
    public String getEmail() {
        return model.getEmail();
    }

    @Override
    public Iterable<? extends AuthorPageViewFirstNameField> getFirstName() {
        return RichTextUtils.buildInlineHtml(
                model,
                PersonAuthor::getFirstName,
                e -> createView(AuthorPageViewFirstNameField.class, e));
    }

    @Override
    public Iterable<? extends AuthorPageViewFullBiographyField> getFullBiography() {
        return RichTextUtils.buildHtml(
            model,
            PersonAuthor::getFullBiography,
            e -> createView(AuthorPageViewFullBiographyField.class, e));
    }

    @Override
    public Iterable<? extends AuthorPageViewImageField> getImage() {
        return createViews(AuthorPageViewImageField.class, model.getImage());
    }

    @Override
    public Iterable<? extends AuthorPageViewJobTitleField> getJobTitle() {
        return RichTextUtils.buildInlineHtml(
                model,
                PersonAuthor::getTitle,
                e -> createView(AuthorPageViewJobTitleField.class, e));
    }

    @Override
    public Iterable<? extends AuthorPageViewLastNameField> getLastName() {
        return RichTextUtils.buildInlineHtml(
                model,
                PersonAuthor::getLastName,
                e -> createView(AuthorPageViewLastNameField.class, e));
    }

    @Override
    public Iterable<? extends AuthorPageViewLinksField> getLinks() {
        return null;
    }

    @Override
    public Iterable<? extends AuthorPageViewNameField> getName() {
        return RichTextUtils.buildInlineHtml(
                model,
                PersonAuthor::getName,
                e -> createView(AuthorPageViewNameField.class, e));
    }

    @Override
    public Iterable<? extends AuthorPageViewShortBiographyField> getShortBiography() {
        return RichTextUtils.buildHtml(model, PersonAuthor::getShortBiography, e -> createView(AuthorPageViewShortBiographyField.class, e));
    }

    @Override
    public CharSequence getJsonLinkedData() {
        return Optional.ofNullable(JsonLd.createHtmlScriptBody(createView(PersonSchemaView.class, model)))
            .map(RawHtml::of)
            .orElse(null);
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return null;
    }
}
