package brightspot.core.oneoffpage;

import brightspot.core.page.AbstractPageViewModel;
import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.styleguide.core.page.PageView;
import com.psddev.styleguide.core.page.PageViewMainField;
import com.psddev.styleguide.core.page.PageViewPageLeadField;

public class OneOffPageViewModel extends AbstractPageViewModel<OneOffPage> implements PageView, PageEntryView {

    @Override
    public Iterable<? extends PageViewMainField> getMain() {

        return createViews(PageViewMainField.class, model.getContents());
    }

    @Override
    public CharSequence getPageHeading() {
        if (!model.isHideDisplayName()) {
            return model.getDisplayName();
        }
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageLeadField> getPageLead() {
        return createViews(PageViewPageLeadField.class, model.getLead());
    }

    @Override
    public CharSequence getPageSubHeading() {
        return RichTextUtils.buildInlineHtml(model.getState().getDatabase(), model.getDescription(), this::createView);
    }
}
