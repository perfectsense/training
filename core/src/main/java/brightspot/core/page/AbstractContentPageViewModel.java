package brightspot.core.page;

import com.psddev.dari.db.Recordable;
import com.psddev.styleguide.core.page.ContentPageView;
import com.psddev.styleguide.core.page.ContentPageViewBrandsField;
import com.psddev.styleguide.core.page.ContentPageViewBreadcrumbsField;
import com.psddev.styleguide.core.page.ContentPageViewCommentsField;
import com.psddev.styleguide.core.page.ContentPageViewCountriesField;
import com.psddev.styleguide.core.page.ContentPageViewProductsField;
import com.psddev.styleguide.core.page.ContentPageViewTagsField;
import com.psddev.styleguide.core.page.ContentPageViewTopicsField;

public abstract class AbstractContentPageViewModel<M extends Recordable> extends AbstractPageViewModel<M> implements
    ContentPageView {

    @Override
    public Iterable<? extends ContentPageViewBrandsField> getBrands() {
        return page.getBrands(ContentPageViewBrandsField.class);
    }

    @Override
    public Iterable<? extends ContentPageViewBreadcrumbsField> getBreadcrumbs() {
        return page.getBreadcrumbs(ContentPageViewBreadcrumbsField.class);
    }

    @Override
    public Iterable<? extends ContentPageViewCommentsField> getComments() {
        return page.getComments(ContentPageViewCommentsField.class);
    }

    @Override
    public Iterable<? extends ContentPageViewCountriesField> getCountries() {
        return page.getCountries(ContentPageViewCountriesField.class);
    }

    @Override
    public Iterable<? extends ContentPageViewProductsField> getProducts() {
        return page.getProducts(ContentPageViewProductsField.class);
    }

    @Override
    public Iterable<? extends ContentPageViewTagsField> getTags() {
        return page.getTags(ContentPageViewTagsField.class);
    }

    @Override
    public Iterable<? extends ContentPageViewTopicsField> getTopics() {
        return page.getTopics(ContentPageViewTopicsField.class);
    }
}
