package brightspot.rte;

import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.richtext.RichTextElementsTinyView;
import com.psddev.styleguide.richtext.RichTextElementsTinyViewTextField;

public class RichTextElementsTinyViewModel extends ViewModel<RichTextElement> implements RichTextElementsTinyView {

    @Override
    public Iterable<? extends RichTextElementsTinyViewTextField> getText() {
        return createViews(RichTextElementsTinyViewTextField.class, model);
    }
}
