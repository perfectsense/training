package brightspot.core.link;

import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.link.LinkView;

public class LinkableLinkViewModel extends ViewModel<Linkable> implements LinkView {

    @CurrentSite
    Site currentSite;

    @Override
    public CharSequence getHref() {
        // Plain text
        return model.getLinkableUrl(currentSite);
    }

    @Override
    public CharSequence getBody() {
        return RichTextUtils.buildInlineHtml(model.getState().getDatabase(), model.getLinkableText(), this::createView);
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
