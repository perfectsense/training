package brightspot.section;

import brightspot.link.Linkable;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.page.BreadcrumbLinkView;
import com.psddev.styleguide.page.BreadcrumbLinkViewBodyField;

/**
 * Breadcrumbs field on ContentPage requires BreadcrumbLinkView
 */
public class SectionBreadcrumbViewModel extends ViewModel<Section> implements BreadcrumbLinkView {

    @CurrentSite
    Site currentSite;

    @Override
    public CharSequence getHref() {
        // Plain text
        return model.getLinkableUrl(currentSite);
    }

    @Override
    public Iterable<? extends BreadcrumbLinkViewBodyField> getBody() {
        return RichTextUtils.buildInlineHtml(
                model,
                Linkable::getLinkableText,
                e -> createView(BreadcrumbLinkViewBodyField.class, e));
    }
}
