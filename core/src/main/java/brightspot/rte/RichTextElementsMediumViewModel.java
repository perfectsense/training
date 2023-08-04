package brightspot.rte;

import com.psddev.cms.db.RichTextElement;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.richtext.RichTextElementsMediumView;
import com.psddev.styleguide.richtext.RichTextElementsMediumViewTextField;

public class RichTextElementsMediumViewModel extends ViewModel<RichTextElement> implements RichTextElementsMediumView {

    @Override
    public Iterable<? extends RichTextElementsMediumViewTextField> getText() {
        return createViews(RichTextElementsMediumViewTextField.class, model);
    }
}
