package brightspot.core.byline;

import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.link.LinkView;

public class BylineLinkViewModel extends ViewModel<Byline> implements LinkView {

    @CurrentSite
    protected Site site;

    @Override
    public CharSequence getHref() {
        return model.getBylineUrl(site);
    }

    @Override
    public CharSequence getBody() {
        return model.getBylineName();
    }

    @Override
    public CharSequence getTarget() {
        return null;
    }

    @Override
    public CharSequence getContentId() {
        return null;
    }
}
