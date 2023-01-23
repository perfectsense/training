package brightspot.rss;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.itemstream.ItemStream;
import brightspot.itemstream.ItemStreamItemWrapper;
import brightspot.l10n.LocaleProvider;
import brightspot.module.ModulePlacement;
import brightspot.module.list.page.AbstractPageListModule;
import brightspot.module.list.page.DynamicPageItemStream;
import brightspot.module.list.page.PageItemStreamItem;
import brightspot.module.list.page.PageListModulePlacementInline;
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
        ItemStream<PageItemStreamItem> defaultPageItemStream,
        List<ModulePlacement> contents,
        Site site) {

        ItemStream<PageItemStreamItem> itemStream = defaultPageItemStream;

        if (contents != null && !contents.isEmpty()) {

            ItemStream<PageItemStreamItem> contentsItemStream = contents.stream()
                .filter(modulePlacement -> modulePlacement instanceof PageListModulePlacementInline)
                .map(modulePlacement -> (PageListModulePlacementInline) modulePlacement)
                .map(AbstractPageListModule::getItemStream)
                .filter(Objects::nonNull)
                .filter(is -> is.isInstantiableTo(DynamicPageItemStream.class))
                .findFirst()
                .orElse(null);

            if (contentsItemStream != null) {
                itemStream = contentsItemStream;
            }
        }

        if (itemStream != null) {
            return itemStream.getItems(site, this, 0, itemStream.getItemsPerPage(site, this))
                .stream()
                .filter(ItemStreamItemWrapper.class::isInstance)
                .map(ItemStreamItemWrapper.class::cast)
                .map(ItemStreamItemWrapper::getItemStreamItem)
                .filter(wrappedObj -> wrappedObj.isInstantiableTo(FeedItem.class))
                .map(item -> item.as(FeedItem.class))
                .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
