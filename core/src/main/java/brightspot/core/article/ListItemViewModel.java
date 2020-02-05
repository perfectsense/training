package brightspot.core.article;

import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.article.ListArticleBodyItemView;
import com.psddev.styleguide.core.article.ListArticleBodyItemViewBodyField;

public class ListItemViewModel extends ViewModel<ListItem> implements ListArticleBodyItemView {

    @Override
    public CharSequence getHeadline() {
        // Plain text
        return model.getHeadline();
    }

    @Override
    public Iterable<? extends ListArticleBodyItemViewBodyField> getBody() {
        return RichTextUtils.buildHtml(
            model.getState().getDatabase(),
            model.getBody(),
            e -> createView(ListArticleBodyItemViewBodyField.class, e));
    }
}
