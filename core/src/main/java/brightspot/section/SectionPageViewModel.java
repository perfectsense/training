package brightspot.section;

import brightspot.page.AbstractPageViewModel;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.jsonld.JsonLdType;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewPageHeadingField;
import com.psddev.styleguide.page.PageViewPageLeadField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import com.psddev.styleguide.section.SectionPageView;

@JsonLdType("WebPage")
public class SectionPageViewModel extends AbstractPageViewModel<SectionPage> implements SectionPageView, PageEntryView {

    @Override
    public Iterable<? extends PageViewMainField> getMain() {
        return createViews(PageViewMainField.class, model.getContents());
    }

    @Override
    public Iterable<? extends PageViewPageHeadingField> getPageHeading() {
        return RichTextUtils.buildInlineHtml(
            model,
            SectionPage::getDisplayName,
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
            SectionPage::getDescription,
            e -> createView(PageViewPageSubHeadingField.class, e));
    }
}
