package bex.training.core.simplepromo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.product.ProductLaunchModuleView;
import com.psddev.styleguide.core.product.ProductLaunchModuleViewMediaField;

public class ProductLaunchModuleViewModel extends ViewModel<ProductLaunchModule> implements ProductLaunchModuleView {

    @Override
    public CharSequence getTitle() {
        return model.getTitle();
    }

    @Override
    public CharSequence getDescription() {
        return model.getDescription();
    }

    @Override
    public Iterable<? extends ProductLaunchModuleViewMediaField> getMedia() {
        return createViews(ProductLaunchModuleViewMediaField.class, model.getMedia());
    }

    @Override
    public CharSequence getEndDate() {

        if (model.getEndDate() != null) {
            String endDateFormat = "MM/dd/yyyy hh:mm:ss a";
            DateFormat df = new SimpleDateFormat(endDateFormat);
            return df.format(model.getEndDate());
        }

        return null;
    }
}
