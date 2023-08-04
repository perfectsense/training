package brightspot.genericpage;

import java.util.Optional;
import java.util.function.Function;

import brightspot.page.AbstractPageHeadingViewModel;
import brightspot.page.CurrentPageViewModel;
import brightspot.page.PageHeading;
import brightspot.page.PageViewModel;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.styleguide.page.PageHeadingView;

public class GenericPageHeadingViewModel extends AbstractPageHeadingViewModel<GenericPage> implements PageHeadingView {

    @CurrentSite
    protected Site site;

    @CurrentPageViewModel(PageViewModel.class)
    protected PageViewModel page;

    @Override
    public PageHeading getPageHeadingObject() {
        return Optional.ofNullable(model.getLead())
            .filter(PageHeading.class::isInstance)
            .map(PageHeading.class::cast)
            .orElse(null);
    }

    @Override
    public Function<GenericPage, String> getSubHeadingAccessor() {
        return GenericPage::getDescription;
    }

    @Override
    public Function<GenericPage, String> getTitleAccessor() {
        return GenericPage::getDisplayName;
    }
}
