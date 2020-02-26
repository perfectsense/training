package brightspot.core.footer;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.footer.FooterColumnsView;
import com.psddev.styleguide.core.footer.FooterColumnsViewColumnsField;

public class FooterColumnsViewModel extends ViewModel<FooterColumns> implements FooterColumnsView {

    @Override
    public Iterable<? extends FooterColumnsViewColumnsField> getColumns() {
        return createViews(FooterColumnsViewColumnsField.class, model.getColumns());
    }
}
