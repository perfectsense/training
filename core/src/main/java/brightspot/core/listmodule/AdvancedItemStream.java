package brightspot.core.listmodule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import brightspot.core.module.ModuleType;
import brightspot.core.promo.Promo;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Advanced")
public class AdvancedItemStream extends AbstractListItemStream implements
    Interchangeable,
    ListModuleItemStream {

    @ToolUi.Unlabeled
    private List<ModuleType> items;

    @Override
    public List<ModuleType> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    @Override
    public int getItemsPerPage(Site site, Object mainObject) {
        return getItems().size();
    }

    @Override
    public boolean loadTo(Object newObj) {
        if (newObj instanceof DynamicItemStream) {
            ((DynamicItemStream) newObj).getPinnedItems().addAll(0, getItems().stream()
                .filter(Promo.class::isInstance)
                .map(Promo.class::cast)
                .collect(Collectors.toList()));
            return true;
        }
        if (newObj instanceof SimpleItemStream) {
            ((SimpleItemStream) newObj).getItems().addAll(getItems().stream()
                .filter(Promo.class::isInstance)
                .map(Promo.class::cast)
                .map(promo -> promo.getPromotable().orElse(null))
                .collect(Collectors.toList()));

            return true;
        }
        return false;
    }
}
