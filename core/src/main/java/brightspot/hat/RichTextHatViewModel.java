package brightspot.hat;

import brightspot.util.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.page.PageHeaderTextHatView;
import com.psddev.styleguide.page.PageHeaderTextHatViewTextField;

public class RichTextHatViewModel extends ViewModel<RichTextHat> implements PageHeaderTextHatView {

    @Override
    public Iterable<? extends PageHeaderTextHatViewTextField> getText() {
        return RichTextUtils.buildHtml(
            model,
            RichTextHat::getText,
            e -> createView(PageHeaderTextHatViewTextField.class, e));
    }
}
