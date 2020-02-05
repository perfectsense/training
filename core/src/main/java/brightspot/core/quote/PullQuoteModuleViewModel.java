package brightspot.core.quote;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.quote.QuoteView;
import com.psddev.styleguide.core.quote.QuoteViewImageField;

public class PullQuoteModuleViewModel extends ViewModel<PullQuoteModule> implements QuoteView {

    @Override
    public Iterable<? extends QuoteViewImageField> getImage() {
        return createViews(QuoteViewImageField.class, model.getAttributionImage());
    }

    @Override
    public CharSequence getQuote() {
        return model.getQuote();
    }

    @Override
    public CharSequence getAttribution() {
        return model.getAttribution();
    }
}
