package brightspot.module;

import java.util.Optional;

import brightspot.module.carousel.CarouselModulePlacementShared;
import brightspot.module.container.ContainerModulePlacementShared;
import brightspot.module.container.SharedModuleContainerPlacement;
import brightspot.module.faq.FaqModulePlacementShared;
import brightspot.module.list.attachment.shared.AttachmentListModulePlacementShared;
import brightspot.module.list.author.AuthorListModulePlacementShared;
import brightspot.module.list.logo.LogoImageListModulePlacementShared;
import brightspot.module.list.page.PageListModulePlacementShared;
import brightspot.module.list.person.PersonListModulePlacementShared;
import brightspot.module.list.podcast.shared.PodcastEpisodeListModulePlacementShared;
import brightspot.module.list.podcast.shared.PodcastListModulePlacementShared;
import brightspot.module.list.quote.QuoteListModulePlacementShared;
import brightspot.module.list.stat.StatListModulePlacementShared;
import brightspot.module.promo.page.PagePromoModulePlacementShared;
import brightspot.module.promo.page.dynamic.shared.DynamicPagePromoModulePlacementShared;
import brightspot.module.promo.person.PersonPromoModulePlacementShared;
import brightspot.module.richtext.RichTextModulePlacementShared;
import brightspot.module.search.SearchResultsModulePlacementShared;
import brightspot.module.tabs.SharedModuleTabPlacement;
import brightspot.module.tabs.TabsModulePlacementShared;
import brightspot.playlist.video.VideoPlaylistPlacementShared;
import com.psddev.dari.db.StateValueAdapter;

final class SharedModuleStateValueAdapters {

    static final class CarouselModulePlacementSharedPageAdapter
        implements StateValueAdapter<CarouselModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(CarouselModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class CarouselModulePlacementSharedContainerAdapter
        implements StateValueAdapter<CarouselModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(CarouselModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class CarouselModulePlacementSharedTabsAdapter
        implements StateValueAdapter<CarouselModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(CarouselModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class FaqModulePlacementSharedPageAdapter
        implements StateValueAdapter<FaqModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(FaqModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class FaqModulePlacementSharedContainerAdapter
        implements StateValueAdapter<FaqModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(FaqModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class FaqModulePlacementSharedTabsAdapter
        implements StateValueAdapter<FaqModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(FaqModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class AuthorListModulePlacementSharedPageAdapter
        implements StateValueAdapter<AuthorListModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(AuthorListModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class AuthorListModulePlacementSharedContainerAdapter
        implements StateValueAdapter<AuthorListModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(AuthorListModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class AuthorListModulePlacementSharedTabsAdapter
        implements StateValueAdapter<AuthorListModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(AuthorListModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class LogoImageListModulePlacementSharedPageAdapter
        implements StateValueAdapter<LogoImageListModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(LogoImageListModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class LogoImageListModulePlacementSharedContainerAdapter
        implements StateValueAdapter<LogoImageListModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(LogoImageListModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class LogoImageListModulePlacementSharedTabsAdapter
        implements StateValueAdapter<LogoImageListModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(LogoImageListModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PageListModulePlacementSharedPageAdapter
        implements StateValueAdapter<PageListModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(PageListModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PageListModulePlacementSharedContainerAdapter
        implements StateValueAdapter<PageListModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(PageListModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PageListModulePlacementSharedTabsAdapter
        implements StateValueAdapter<PageListModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(PageListModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PodcastEpisodeListModulePlacementSharedPageAdapter
        implements StateValueAdapter<PodcastEpisodeListModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(PodcastEpisodeListModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PodcastEpisodeListModulePlacementSharedContainerAdapter
        implements StateValueAdapter<PodcastEpisodeListModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(PodcastEpisodeListModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PodcastEpisodeListModulePlacementSharedTabsAdapter
        implements StateValueAdapter<PodcastEpisodeListModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(PodcastEpisodeListModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PodcastListModulePlacementSharedPageAdapter
        implements StateValueAdapter<PodcastListModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(PodcastListModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PodcastListModulePlacementSharedContainerAdapter
        implements StateValueAdapter<PodcastListModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(PodcastListModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PodcastListModulePlacementSharedTabsAdapter
        implements StateValueAdapter<PodcastListModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(PodcastListModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class QuoteListModulePlacementSharedPageAdapter
        implements StateValueAdapter<QuoteListModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(QuoteListModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class QuoteListModulePlacementSharedContainerAdapter
        implements StateValueAdapter<QuoteListModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(QuoteListModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class QuoteListModulePlacementSharedTabsAdapter
        implements StateValueAdapter<QuoteListModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(QuoteListModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class StatListModulePlacementSharedPageAdapter
        implements StateValueAdapter<StatListModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(StatListModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class StatListModulePlacementSharedContainerAdapter
        implements StateValueAdapter<StatListModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(StatListModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class StatListModulePlacementSharedTabsAdapter
        implements StateValueAdapter<StatListModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(StatListModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class DynamicPagePromoModulePlacementSharedPageAdapter
        implements StateValueAdapter<DynamicPagePromoModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(DynamicPagePromoModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class DynamicPagePromoModulePlacementSharedContainerAdapter
        implements StateValueAdapter<DynamicPagePromoModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(DynamicPagePromoModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class DynamicPagePromoModulePlacementSharedTabsAdapter
        implements StateValueAdapter<DynamicPagePromoModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(DynamicPagePromoModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PagePromoModulePlacementSharedPageAdapter
        implements StateValueAdapter<PagePromoModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(PagePromoModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PagePromoModulePlacementSharedContainerAdapter
        implements StateValueAdapter<PagePromoModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(PagePromoModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PagePromoModulePlacementSharedTabsAdapter
        implements StateValueAdapter<PagePromoModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(PagePromoModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PersonPromoModulePlacementSharedPageAdapter
        implements StateValueAdapter<PersonPromoModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(PersonPromoModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PersonPromoModulePlacementSharedContainerAdapter
        implements StateValueAdapter<PersonPromoModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(PersonPromoModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PersonPromoModulePlacementSharedTabsAdapter
        implements StateValueAdapter<PersonPromoModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(PersonPromoModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class RichTextModulePlacementSharedPageAdapter
        implements StateValueAdapter<RichTextModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(RichTextModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class RichTextModulePlacementSharedContainerAdapter
        implements StateValueAdapter<RichTextModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(RichTextModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class RichTextModulePlacementSharedTabsAdapter
        implements StateValueAdapter<RichTextModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(RichTextModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class SearchResultsModulePlacementSharedPageAdapter
        implements StateValueAdapter<SearchResultsModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(SearchResultsModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class SearchResultsModulePlacementSharedContainerAdapter
        implements StateValueAdapter<SearchResultsModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(SearchResultsModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class SearchResultsModulePlacementSharedTabsAdapter
        implements StateValueAdapter<SearchResultsModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(SearchResultsModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class TabsModulePlacementSharedPageAdapter
        implements StateValueAdapter<TabsModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(TabsModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class TabsModulePlacementSharedContainerAdapter
        implements StateValueAdapter<TabsModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(TabsModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class TabsModulePlacementSharedTabsAdapter
        implements StateValueAdapter<TabsModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(TabsModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class VideoPlaylistPlacementSharedPageAdapter
        implements StateValueAdapter<VideoPlaylistPlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(VideoPlaylistPlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class VideoPlaylistPlacementSharedContainerAdapter
        implements StateValueAdapter<VideoPlaylistPlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(VideoPlaylistPlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class VideoPlaylistPlacementSharedTabsAdapter
        implements StateValueAdapter<VideoPlaylistPlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(VideoPlaylistPlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class ContainerModulePlacementSharedPageAdapter
        implements StateValueAdapter<ContainerModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(ContainerModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(Optional.ofNullable(source.getState())
                .map(state -> state.get("shared"))
                .filter(SharedModule.class::isInstance)
                .map(SharedModule.class::cast)
                .orElse(null));
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class ContainerModulePlacementSharedContainerAdapter
        implements StateValueAdapter<ContainerModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(ContainerModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(Optional.ofNullable(source.getState())
                .map(state -> state.get("shared"))
                .filter(SharedModule.class::isInstance)
                .map(SharedModule.class::cast)
                .orElse(null));
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class ContainerModulePlacementSharedTabsAdapter
        implements StateValueAdapter<ContainerModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(ContainerModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(Optional.ofNullable(source.getState())
                .map(state -> state.get("shared"))
                .filter(SharedModule.class::isInstance)
                .map(SharedModule.class::cast)
                .orElse(null));
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PersonListModulePlacementSharedPageAdapter
        implements StateValueAdapter<PersonListModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(PersonListModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PersonListModulePlacementSharedContainerAdapter
        implements StateValueAdapter<PersonListModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(PersonListModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class PersonListModulePlacementSharedTabsAdapter
        implements StateValueAdapter<PersonListModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(PersonListModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class AttachmentListModulePlacementSharedPageAdapter
        implements StateValueAdapter<AttachmentListModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(AttachmentListModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class AttachmentListModulePlacementSharedContainerAdapter
        implements StateValueAdapter<AttachmentListModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(AttachmentListModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class AttachmentListModulePlacementSharedTabsAdapter
        implements StateValueAdapter<AttachmentListModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(AttachmentListModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.setSharedModule(source.getShared());
            pagePlacement.getState().setId(source.getState().getId());
            return pagePlacement;
        }
    }

    static final class CustomModulePlacementSharedPageAdapter
        implements StateValueAdapter<CustomModulePlacementShared, SharedModulePagePlacement> {

        @Override
        public SharedModulePagePlacement adapt(CustomModulePlacementShared source) {
            SharedModulePagePlacement pagePlacement = new SharedModulePagePlacement();
            pagePlacement.getState().setId(source.getState().getId());
            //Old implementation was broken so this is the best we can do
            return pagePlacement;
        }
    }

    static final class CustomModulePlacementSharedContainerAdapter
        implements StateValueAdapter<CustomModulePlacementShared, SharedModuleContainerPlacement> {

        @Override
        public SharedModuleContainerPlacement adapt(CustomModulePlacementShared source) {
            SharedModuleContainerPlacement pagePlacement = new SharedModuleContainerPlacement();
            pagePlacement.getState().setId(source.getState().getId());
            //Old implementation was broken so this is the best we can do
            return pagePlacement;
        }
    }

    static final class CustomModulePlacementSharedTabsAdapter
        implements StateValueAdapter<CustomModulePlacementShared, SharedModuleTabPlacement> {

        @Override
        public SharedModuleTabPlacement adapt(CustomModulePlacementShared source) {
            SharedModuleTabPlacement pagePlacement = new SharedModuleTabPlacement();
            pagePlacement.getState().setId(source.getState().getId());
            //Old implementation was broken so this is the best we can do
            return pagePlacement;
        }
    }

}
