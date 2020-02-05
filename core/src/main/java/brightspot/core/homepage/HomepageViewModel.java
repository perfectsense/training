package brightspot.core.homepage;

import brightspot.core.page.AbstractPageViewModel;
import com.psddev.cms.view.PageEntryView;
import com.psddev.styleguide.core.page.PageViewMainField;
import com.psddev.styleguide.core.page.PageViewPageLeadField;

public class HomepageViewModel extends AbstractPageViewModel<Homepage> implements PageEntryView {

    @Override
    public Iterable<? extends PageViewPageLeadField> getPageLead() {
        return createViews(PageViewPageLeadField.class, model.getLead());
    }

    @Override
    public Iterable<? extends PageViewMainField> getMain() {
        return createViews(PageViewMainField.class, model.getContent());
    }

    @Override
    public CharSequence getPageHeading() {
        return null;
    }

    @Override
    public CharSequence getPageSubHeading() {
        return null;
    }
}
