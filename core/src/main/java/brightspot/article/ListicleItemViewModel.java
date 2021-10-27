package brightspot.article;

import brightspot.util.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.listicle.ListicleItemView;
import com.psddev.styleguide.listicle.ListicleItemViewBodyField;
import com.psddev.styleguide.listicle.ListicleItemViewTitleField;

public class ListicleItemViewModel extends ViewModel<ListicleItem> implements ListicleItemView {

    @Override
    public Iterable<? extends ListicleItemViewBodyField> getBody() {
        return RichTextUtils.buildHtml(
            model.getListicle(),
            listicle -> model.getBody(),
            e -> createView(ListicleItemViewBodyField.class, e)
        );
    }

    @Override
    public Iterable<? extends ListicleItemViewTitleField> getTitle() {
        return RichTextUtils.buildInlineHtml(
            model.getListicle(),
            listicle -> model.getTitle(),
            e -> createView(ListicleItemViewTitleField.class, e)
        );
    }
}
