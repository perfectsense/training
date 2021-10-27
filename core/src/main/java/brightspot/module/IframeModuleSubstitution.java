package brightspot.module;

import brightspot.module.iframe.IframeEmbedModulePlacementShared;
import brightspot.module.list.page.PageItemStreamItem;
import brightspot.promo.page.PagePromotable;
import com.psddev.dari.util.Substitution;

public class IframeModuleSubstitution extends IframeEmbedModulePlacementShared implements PageItemStreamItem,
    Substitution {

    // --- ItemStreamItemWrapper support ---

    @Override
    public PagePromotable getItemStreamItem() {
        return null;
    }
}
