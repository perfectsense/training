package brightspot.core.article;

import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.article.RichTextArticleBodyView;
import com.psddev.styleguide.core.article.RichTextArticleBodyViewBodyField;

public class RichTextBodyViewModel extends ViewModel<RichTextBody> implements RichTextArticleBodyView {

    @Override
    public Iterable<? extends RichTextArticleBodyViewBodyField> getBody() {
        return RichTextUtils.buildHtml(
            model.getState().getDatabase(),
            model.getRichText(),
            e -> createView(RichTextArticleBodyViewBodyField.class, e));
    }
}
