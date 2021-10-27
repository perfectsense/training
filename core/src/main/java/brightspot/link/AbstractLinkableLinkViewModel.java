package brightspot.link;

import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.link.LinkView;
import com.psddev.styleguide.link.LinkViewBodyField;

public abstract class AbstractLinkableLinkViewModel<M extends Linkable> extends ViewModel<M> implements LinkView {

    @CurrentSite
    Site currentSite;

    @Override
    public CharSequence getHref() {
        // Plain text
        return model.getLinkableUrl(currentSite);
    }

    @Override
    public Iterable<? extends LinkViewBodyField> getBody() {
        return RichTextUtils.buildInlineHtml(
                model,
                Linkable::getLinkableText,
                e -> createView(LinkViewBodyField.class, e));
    }

    @Override
    public CharSequence getTarget() {
        return null;
    }

    @Override
    public CharSequence getContentId() {
        return model.getState().getId().toString();
    }
}
