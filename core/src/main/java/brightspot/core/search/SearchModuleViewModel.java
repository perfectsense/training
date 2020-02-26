package brightspot.core.search;

import java.util.Optional;

import brightspot.core.site.FrontEndSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.search.SearchModuleView;

public class SearchModuleViewModel extends ViewModel<SearchModule> implements
    SearchModuleView {

    @CurrentSite
    private Site currentSite;

    @Override
    public CharSequence getSearchAction() {
        return Optional.ofNullable(FrontEndSettings.get(currentSite, FrontEndSettings::getSearchPage))
            .map(search -> search.getSearchAction(currentSite))
            .orElse(null);
    }

    @Override
    public CharSequence getPlaceholder() {
        return model.getPlaceholder();
    }
}
