package brightspot.page;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.page.PageHrefLangLinkView;
import org.jooq.tools.StringUtils;

public class PageHrefLangLinkViewModel extends ViewModel<PageHrefLangLink> implements PageHrefLangLinkView {

    @Override
    protected boolean shouldCreate() {
        return !(StringUtils.isBlank(model.getLanguageCode()) || StringUtils.isBlank(model.getUrl()));
    }

    @Override
    public CharSequence getLanguageCode() {
        return model.getLanguageCode();
    }

    @Override
    public CharSequence getUrl() {
        return model.getUrl();
    }
}
