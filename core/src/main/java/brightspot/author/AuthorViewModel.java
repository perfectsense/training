package brightspot.author;

import brightspot.permalink.Permalink;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.author.AuthorView;
import com.psddev.styleguide.author.AuthorViewBiographyField;
import com.psddev.styleguide.author.AuthorViewImageField;
import com.psddev.styleguide.author.AuthorViewNameField;

public class AuthorViewModel extends ViewModel<Author> implements AuthorView {

    @CurrentSite
    private Site site;

    @Override
    public Iterable<? extends AuthorViewBiographyField> getBiography() {
        return RichTextUtils.buildInlineHtml(
                model,
                Author::getShortBiography,
                e -> createView(AuthorViewBiographyField.class, e));
    }

    @Override
    public Iterable<? extends AuthorViewImageField> getImage() {
        return createViews(AuthorViewImageField.class, model.getImage());
    }

    @Override
    public Iterable<? extends AuthorViewNameField> getName() {
        return RichTextUtils.buildInlineHtml(
                model,
                Author::getName,
                e -> createView(AuthorViewNameField.class, e));
    }

    @Override
    public CharSequence getUrl() {
        return Permalink.getPermalink(site, model);
    }
}
