package brightspot.core.quote;

import java.util.Optional;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.quote.QuoteView;
import com.psddev.styleguide.core.quote.QuoteViewImageField;

public class QuoteRichTextElementViewModel extends ViewModel<QuoteRichTextElement> implements QuoteView {

    @Override
    public CharSequence getQuote() {
        // Plain text
        return model.getQuote();
    }

    @Override
    public CharSequence getAttribution() {
        // Plain text
        return model.getAttribution();
    }

    @Override
    public Iterable<? extends QuoteViewImageField> getImage() {
        return Optional.ofNullable(model.getImage())
            .map(imageOption -> createViews(QuoteViewImageField.class, imageOption))
            .orElse(null);
    }

}
