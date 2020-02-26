package brightspot.core.rss;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.listmodule.DynamicItemStream;
import brightspot.core.listmodule.ItemStream;
import brightspot.core.listmodule.ListModule;
import brightspot.core.module.ModuleType;
import brightspot.core.site.FrontEndSettings;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.feed.FeedItem;
import com.psddev.feed.FeedSource;

public interface DynamicFeedSource extends FeedSource {

    default String getDynamicFeedLanguage(Site site) {
        return Optional.ofNullable(FrontEndSettings.get(site, FrontEndSettings::getLocale))
            .orElseGet(Locale::getDefault)
            .toLanguageTag();
    }

    default List<FeedItem> getFeedFromDynamicStream(ItemStream itemStream, List<ModuleType> contents, Site site) {
        if (contents != null && !contents.isEmpty()) {
            Optional<ListModule> list = contents.stream()
                .filter(moduleType -> moduleType instanceof ListModule)
                .map(moduleType -> (ListModule) moduleType)
                .filter(listModule -> listModule.getItemStream() instanceof DynamicItemStream)
                .findFirst();

            if (list.isPresent()) {
                itemStream = list.get().getItemStream();
            }
        }

        if (itemStream != null) {
            return itemStream.getItems(site, this, 0, itemStream.getItemsPerPage(site, this))
                .stream()
                .filter(item -> State.getInstance(item).isInstantiableTo(FeedItem.class))
                .map(item -> ((Recordable) item).as(FeedItem.class))
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
