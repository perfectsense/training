package brightspot.core.text;

import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.text.RichTextModuleView;
import com.psddev.styleguide.core.text.RichTextModuleViewItemsField;

public class RichTextModuleViewModel extends ViewModel<RichTextModule> implements RichTextModuleView {

    @Override
    public Iterable<? extends RichTextModuleViewItemsField> getItems() {
        return RichTextUtils.buildHtml(
            model.getState().getDatabase(),
            model.getRichText(),
            e -> createView(RichTextModuleViewItemsField.class, e));
    }
}
