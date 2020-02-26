package brightspot.core.article;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.article.ListArticleBodyView;
import com.psddev.styleguide.core.article.ListArticleBodyViewItemsField;

public class ListBodyViewModel extends ViewModel<ListBody> implements ListArticleBodyView {

    @Override
    public Iterable<? extends ListArticleBodyViewItemsField> getItems() {
        return createViews(ListArticleBodyViewItemsField.class, model.getItems());
    }
}
