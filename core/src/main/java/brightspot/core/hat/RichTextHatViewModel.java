package brightspot.core.hat;

import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.page.PageHeaderTextHatView;

public class RichTextHatViewModel
    extends ViewModel<RichTextHat>
    implements PageHeaderTextHatView {

    @Override
    public CharSequence getText() {
        return RichTextUtils.buildInlineHtml(model.getState().getDatabase(), model.getText(), this::createView);
    }
}
