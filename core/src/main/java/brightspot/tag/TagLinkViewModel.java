package brightspot.tag;

import brightspot.link.Linkable;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.tag.TagLinkView;
import com.psddev.styleguide.tag.TagLinkViewBodyField;

/**
 * Tags field on ContentPage requires TagLinkView
 */
public class TagLinkViewModel extends ViewModel<Tag> implements TagLinkView {

    @CurrentSite
    Site currentSite;

    @Override
    public CharSequence getHref() {
        // Plain text
        return model.getLinkableUrl(currentSite);
    }

    @Override
    public Iterable<? extends TagLinkViewBodyField> getBody() {
        return RichTextUtils.buildInlineHtml(
                model,
                Linkable::getLinkableText,
                e -> createView(TagLinkViewBodyField.class, e));
    }
}
