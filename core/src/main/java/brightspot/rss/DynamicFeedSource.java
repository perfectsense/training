package brightspot.rss;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.itemstream.ItemStream;
import brightspot.l10n.LocaleProvider;
import brightspot.module.ModulePlacement;
import brightspot.module.list.page.DynamicPageItemStream;
import brightspot.module.list.page.PageItemStreamItem;
import brightspot.module.list.page.PageListModule;
import com.psddev.cms.db.Site;
import com.psddev.feed.FeedItem;
import com.psddev.feed.FeedSource;

public interface DynamicFeedSource extends FeedSource {

    default String getDynamicFeedLanguage(Site site) {
        return Optional.ofNullable(LocaleProvider.getModelLocale(site, this))
            .orElseGet(Locale::getDefault)
            .toLanguageTag();
    }

    default List<FeedItem> getFeedFromDynamicStream(
        ItemStream<PageItemStreamItem> itemStream,
        List<ModulePlacement> contents,
        Site site) {
        if (contents != null && !contents.isEmpty()) {
            Optional<PageListModule> list = contents.stream()
                .filter(moduleType -> moduleType instanceof PageListModule)
                .map(moduleType -> (PageListModule) moduleType)
                .filter(listModule -> listModule.getItemStream() instanceof DynamicPageItemStream)
                .findFirst();

            if (list.isPresent()) {
                itemStream = list.get().getItemStream();
            }
        }

        if (itemStream != null) {
            return itemStream.getItems(site, this, 0, itemStream.getItemsPerPage(site, this))
                .stream()
                .map(PageItemStreamItem::getItemStreamItem)
                .filter(Objects::nonNull)
                .filter(promotable -> promotable.isInstantiableTo(FeedItem.class))
                .map(item -> item.as(FeedItem.class))
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
