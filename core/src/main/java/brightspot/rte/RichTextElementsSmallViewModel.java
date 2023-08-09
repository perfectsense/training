package brightspot.rte;

import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.richtext.RichTextElementsSmallView;
import com.psddev.styleguide.richtext.RichTextElementsSmallViewTextField;

public class RichTextElementsSmallViewModel extends ViewModel<RichTextElement> implements RichTextElementsSmallView {

    @Override
    public Iterable<? extends RichTextElementsSmallViewTextField> getText() {
        return createViews(RichTextElementsSmallViewTextField.class, model);
    }
}
