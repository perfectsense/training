package brightspot.module.promo.page.dynamic.shared;

import java.util.Optional;

import brightspot.module.ModulePlacement;
import brightspot.module.promo.page.dynamic.DynamicPagePromoModuleShared;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Shared Dynamic Promo")
@Recordable.Embedded
@Deprecated
public class DynamicPagePromoModulePlacementShared extends Record implements
    ModelWrapper,
    ModulePlacement {

    private DynamicPagePromoModuleShared shared;

    public DynamicPagePromoModuleShared getShared() {

        return shared;
    }

    public void setShared(DynamicPagePromoModuleShared shared) {

        this.shared = shared;
    }

    @Override
    public Object unwrap() {

        return getShared();
    }

    @Override
    public String getLabel() {

        return Optional.ofNullable(getShared())
            .map(Record::getLabel)
            .orElse(super.getLabel());
    }
}
