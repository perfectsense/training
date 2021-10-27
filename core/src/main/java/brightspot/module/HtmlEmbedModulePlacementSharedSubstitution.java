package brightspot.module;

import brightspot.module.html.HtmlEmbedModulePlacementShared;
import brightspot.module.list.page.PageItemStreamItem;
import brightspot.promo.page.PagePromotable;
import com.psddev.dari.util.Substitution;

public class HtmlEmbedModulePlacementSharedSubstitution extends HtmlEmbedModulePlacementShared
    implements Substitution, PageItemStreamItem {

    @Override
    public PagePromotable getItemStreamItem() {
        return null;
    }
}
