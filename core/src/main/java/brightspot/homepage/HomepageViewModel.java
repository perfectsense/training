package brightspot.homepage;

import brightspot.page.AbstractPageViewModel;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.jsonld.JsonLdType;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewPageHeadingField;
import com.psddev.styleguide.page.PageViewPageLeadField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;

@JsonLdType("WebPage")
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
    public Iterable<? extends PageViewPageHeadingField> getPageHeading() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return null;
    }
}
