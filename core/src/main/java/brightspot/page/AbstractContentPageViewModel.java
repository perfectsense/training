package brightspot.page;

import com.psddev.dari.db.Recordable;
import com.psddev.styleguide.page.ContentPageView;
import com.psddev.styleguide.page.ContentPageViewBreadcrumbsField;
import com.psddev.styleguide.page.ContentPageViewCommentsField;
import com.psddev.styleguide.page.ContentPageViewTagsField;

public abstract class AbstractContentPageViewModel<M extends Recordable> extends AbstractPageViewModel<M> implements
        ContentPageView {

    @Override
    public Iterable<? extends ContentPageViewBreadcrumbsField> getBreadcrumbs() {
        return page.getBreadcrumbs(ContentPageViewBreadcrumbsField.class);
    }

    @Override
    public Iterable<? extends ContentPageViewCommentsField> getComments() {

        // TODO re-do when Comments support is included
        return null;
    }

    @Override
    public Iterable<? extends ContentPageViewTagsField> getTags() {
        return page.getTags(ContentPageViewTagsField.class);
    }
}
