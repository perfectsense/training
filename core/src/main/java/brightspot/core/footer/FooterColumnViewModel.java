package brightspot.core.footer;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.footer.FooterColumnView;
import com.psddev.styleguide.core.footer.FooterColumnViewItemsField;

public class FooterColumnViewModel extends ViewModel<FooterColumn> implements FooterColumnView {

    @Override
    public Iterable<? extends FooterColumnViewItemsField> getItems() {
        return createViews(FooterColumnViewItemsField.class, model.getItems());
    }
}
