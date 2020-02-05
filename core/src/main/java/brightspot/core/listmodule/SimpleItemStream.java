package brightspot.core.listmodule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import brightspot.core.link.InternalLink;
import brightspot.core.link.Linkable;
import brightspot.core.promo.Promo;
import brightspot.core.promo.Promotable;
import com.psddev.cms.db.Interchangeable;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Basic")
public class SimpleItemStream extends AbstractListItemStream implements
    Interchangeable,
    ListModuleItemStream {

    @ToolUi.Unlabeled
    private List<Promotable> items;

    @Override
    public List<Promotable> getItems() {
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
        List<Promo> promos = getItems().stream()
            .filter(promotable -> promotable.isInstantiableTo(Linkable.class))
            .map(Linkable.class::cast)
            .map(promotable -> {
                Promo promo = new Promo();
                InternalLink internal = new InternalLink();
                internal.setItem(promotable);
                promo.setItem(internal);
                return promo;
            })
            .collect(Collectors.toList());

        if (newObj instanceof AdvancedItemStream) {
            ((AdvancedItemStream) newObj).getItems().addAll(promos);

            return !promos.isEmpty();
        }

        if (newObj instanceof DynamicItemStream) {
            ((DynamicItemStream) newObj).getPinnedItems().addAll(0, promos);

            return !promos.isEmpty();
        }
        return false;
    }
}
