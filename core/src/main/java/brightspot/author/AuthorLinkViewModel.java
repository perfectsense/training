package brightspot.author;

import brightspot.permalink.Permalink;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.link.LinkView;
import com.psddev.styleguide.link.LinkViewBodyField;

/**
 * Authors field on LiveBlogPostView requires LinkView
 */
public class AuthorLinkViewModel extends ViewModel<Author> implements LinkView {

    @CurrentSite
    private Site site;

    @Override
    public Iterable<? extends LinkViewBodyField> getBody() {
        return RichTextUtils.buildInlineHtml(model, Author::getName, e -> createView(LinkViewBodyField.class, e));
    }

    @Override
    public CharSequence getContentId() {
        return model.getState().getId().toString();
    }

    @Override
    public CharSequence getHref() {
        return Permalink.getPermalink(site, model);
    }

    @Override
    public CharSequence getTarget() {
        return null;
    }
}
