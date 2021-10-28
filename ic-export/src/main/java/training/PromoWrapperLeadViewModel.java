package training;

import brightspot.core.promo.PromoWrapper;
import com.psddev.cms.view.ViewModel;

public class PromoWrapperLeadViewModel extends ViewModel<PromoWrapper> implements
    ArticleLeadView,
    ModulePageLeadView {

    @Override
    protected boolean shouldCreate() {
        // GO does not support Promo Leads
        return false;
    }
}
