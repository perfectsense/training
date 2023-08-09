package brightspot.rte;

import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.richtext.RichTextElementsLargeView;
import com.psddev.styleguide.richtext.RichTextElementsLargeViewTextField;

public class RichTextElementsLargeViewModel extends ViewModel<RichTextElement> implements RichTextElementsLargeView {

    @Override
    public Iterable<? extends RichTextElementsLargeViewTextField> getText() {
        return createViews(RichTextElementsLargeViewTextField.class, model);
    }
}
