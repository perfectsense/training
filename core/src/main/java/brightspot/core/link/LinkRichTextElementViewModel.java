package brightspot.core.link;

import java.util.Optional;

import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.RawHtml;
import com.psddev.styleguide.core.link.LinkView;

public class LinkRichTextElementViewModel extends ViewModel<LinkRichTextElement> implements LinkView {

    @CurrentSite
    private Site site;

    @Override
    public CharSequence getBody() {
        return RawHtml.of(model.getLinkText());
    }

    @Override
    public CharSequence getContentId() {
        Link link = model.getLink();
        return (link == null || link instanceof ExternalLink || ((InternalLink) link).getItem() == null)
            ? null
            : ((InternalLink) link).getItem().getState().getId().toString();
    }

    @Override
    public CharSequence getHref() {
        Link link = model.getLink();
        return link == null ? null : link.getLinkUrl(site);
    }

    @Override
    public CharSequence getTarget() {
        return Optional.ofNullable(model.getLink())
            .map(Link::getTarget)
            .map(Target::getValue)
            .orElse(null);
    }
}
