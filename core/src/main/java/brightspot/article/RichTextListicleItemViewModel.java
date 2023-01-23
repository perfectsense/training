package brightspot.article;

import java.util.Optional;

import brightspot.util.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.listicle.ListicleItemView;
import com.psddev.styleguide.listicle.ListicleItemViewBodyField;
import com.psddev.styleguide.listicle.ListicleItemViewTitleField;

public class RichTextListicleItemViewModel extends ViewModel<RichTextListicleItem> implements ListicleItemView {

    @Override
    public Iterable<? extends ListicleItemViewBodyField> getBody() {
        return RichTextUtils.buildHtml(
                model,
                RichTextListicleItem::getBody,
                e -> createView(ListicleItemViewBodyField.class, e)
        );
    }

    @Override
    public CharSequence getItemIndex() {
        return Optional.of(model.as(ListicleItemModification.class).getIndex())
                .map(String::valueOf)
                .orElse(null);
    }

    @Override
    public Iterable<? extends ListicleItemViewTitleField> getTitle() {
        return RichTextUtils.buildInlineHtml(
                model,
                RichTextListicleItem::getHeading,
                e -> createView(ListicleItemViewTitleField.class, e)
        );
    }
}
