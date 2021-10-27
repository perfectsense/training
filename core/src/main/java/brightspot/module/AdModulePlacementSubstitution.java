package brightspot.module;

import brightspot.module.ad.AdModulePlacement;
import brightspot.module.list.page.PageItemStreamItem;
import brightspot.promo.page.PagePromotable;
import com.psddev.dari.util.Substitution;

public class AdModulePlacementSubstitution extends AdModulePlacement implements Substitution, PageItemStreamItem {

    @Override
    public PagePromotable getItemStreamItem() {
        return null;
    }
}
