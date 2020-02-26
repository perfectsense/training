package brightspot.core.person;

import java.util.Optional;

import brightspot.core.page.AbstractContentPageViewModel;
import brightspot.core.seo.PersonSchemaView;
import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.jsonld.JsonLd;
import com.psddev.styleguide.RawHtml;
import com.psddev.styleguide.core.author.AuthorPageView;
import com.psddev.styleguide.core.author.AuthorPageViewAuthorSocialLinksField;
import com.psddev.styleguide.core.author.AuthorPageViewFullBiographyField;
import com.psddev.styleguide.core.author.AuthorPageViewImageField;
import com.psddev.styleguide.core.author.AuthorPageViewLinksField;

public class AuthorPageViewModel extends AbstractContentPageViewModel<Author> implements AuthorPageView, PageEntryView {

    @Override
    public String getAffiliation() {
        return model.getAffiliation();
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
    public CharSequence getFirstName() {
        return model.getFirstName();
    }

    @Override
    public Iterable<? extends AuthorPageViewFullBiographyField> getFullBiography() {
        return RichTextUtils.buildHtml(
            model.getState().getDatabase(),
            model.getFullBiography(),
            e -> createView(AuthorPageViewFullBiographyField.class, e));
    }

    @Override
    public Iterable<? extends AuthorPageViewImageField> getImage() {
        return createViews(AuthorPageViewImageField.class, model.getImage());
    }

    @Override
    public CharSequence getJobTitle() {
        return model.getTitle();
    }

    @Override
    public CharSequence getLastName() {
        return model.getLastName();
    }

    @Override
    public Iterable<? extends AuthorPageViewLinksField> getLinks() {
        return null;
    }

    @Override
    public String getName() {
        return model.getName();
    }

    @Override
    public CharSequence getShortBiography() {
        return RichTextUtils.buildInlineHtml(
            model.getState().getDatabase(),
            model.getShortBiography(),
            this::createView);
    }

    @Override
    public CharSequence getJsonLinkedData() {
        return Optional.ofNullable(JsonLd.createHtmlScriptBody(createView(PersonSchemaView.class, model)))
            .map(RawHtml::of)
            .orElse(null);
    }

    @Override
    public CharSequence getPageSubHeading() {
        return null;
    }
}
