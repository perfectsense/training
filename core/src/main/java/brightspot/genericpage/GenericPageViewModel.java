package brightspot.genericpage;

import brightspot.page.AbstractPageViewModel;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.styleguide.page.PageView;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewPageHeadingField;
import com.psddev.styleguide.page.PageViewPageLeadField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;

public class GenericPageViewModel extends AbstractPageViewModel<GenericPage> implements PageView, PageEntryView {

    @Override
    public Iterable<? extends PageViewMainField> getMain() {

        return createViews(PageViewMainField.class, model.getContents());
    }

    @Override
    public Iterable<? extends PageViewPageHeadingField> getPageHeading() {
        return RichTextUtils.buildInlineHtml(
            model,
            GenericPage::getDisplayName,
            e -> createView(PageViewPageHeadingField.class, e));
    }

    @Override
    public Iterable<? extends PageViewPageLeadField> getPageLead() {
        return createViews(PageViewPageLeadField.class, model.getLead());
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return RichTextUtils.buildHtml(
            model,
            GenericPage::getDescription,
            e -> createView(PageViewPageSubHeadingField.class, e));
    }
}
