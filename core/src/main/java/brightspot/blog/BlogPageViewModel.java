package brightspot.blog;

import brightspot.page.AbstractPageViewModel;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.styleguide.blog.BlogPageView;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewPageHeadingField;
import com.psddev.styleguide.page.PageViewPageLeadField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;

public class BlogPageViewModel extends AbstractPageViewModel<BlogPage> implements PageEntryView, BlogPageView {

    @Override
    public Iterable<? extends PageViewMainField> getMain() {
        return createViews(PageViewMainField.class, model.getContents());
    }

    @Override
    public Iterable<? extends PageViewPageHeadingField> getPageHeading() {
        return RichTextUtils.buildInlineHtml(
                model,
                BlogPage::getDisplayName,
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
                BlogPage::getDescription,
                e -> createView(PageViewPageSubHeadingField.class, e));
    }
}
