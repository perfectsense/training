package brightspot.podcast;

import java.util.Optional;

import brightspot.module.list.podcast.PodcastPromo;
import brightspot.page.AbstractPageViewModel;
import brightspot.promo.podcast.PodcastPromotable;
import brightspot.util.RichTextUtils;
import com.psddev.cms.view.PageEntryView;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewPageHeadingField;
import com.psddev.styleguide.page.PageViewPageLeadField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import com.psddev.styleguide.podcast.PodcastEpisodePageView;
import com.psddev.styleguide.podcast.PodcastEpisodePageViewBodyField;
import com.psddev.styleguide.podcast.PodcastEpisodePageViewBreadcrumbsField;
import com.psddev.styleguide.podcast.PodcastEpisodePageViewHeadlineField;
import com.psddev.styleguide.podcast.PodcastEpisodePageViewImageField;
import com.psddev.styleguide.podcast.PodcastEpisodePageViewPodcastField;
import com.psddev.styleguide.podcast.PodcastEpisodePageViewPrimaryAudioField;
import com.psddev.styleguide.podcast.PodcastEpisodePageViewSubHeadlineField;

public class AudioPodcastEpisodePageViewModel extends AbstractPageViewModel<AudioPodcastEpisodePage> implements PageEntryView,
        PodcastEpisodePageView {

    @Override
    public Iterable<? extends PageViewMainField> getMain() {
        return createViews(PageViewMainField.class, model.getContents());
    }

    @Override
    public Iterable<? extends PageViewPageHeadingField> getPageHeading() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageLeadField> getPageLead() {
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        return null;
    }

    @Override
    public Iterable<? extends PodcastEpisodePageViewBodyField> getBody() {
        return RichTextUtils.buildHtml(
                model, AudioPodcastEpisodePage::getBody,
                e -> createView(PodcastEpisodePageViewBodyField.class, e));
    }

    @Override
    public Iterable<? extends PodcastEpisodePageViewBreadcrumbsField> getBreadcrumbs() {
        return page.getBreadcrumbs(PodcastEpisodePageViewBreadcrumbsField.class);
    }

    @Override
    public Iterable<? extends PodcastEpisodePageViewHeadlineField> getHeadline() {
        return RichTextUtils.buildInlineHtml(
                model, AudioPodcastEpisodePage::getTitle,
                e -> createView(PodcastEpisodePageViewHeadlineField.class, e));
    }

    @Override
    public Iterable<? extends PodcastEpisodePageViewImageField> getImage() {
        return Optional.ofNullable(model)
                .map(AudioPodcastEpisodePage::getPodcastEpisodeCoverImage)
                .map(coverArt -> createViews(PodcastEpisodePageViewImageField.class, coverArt))
                .orElse(null);
    }

    @Override
    public Iterable<? extends PodcastEpisodePageViewPodcastField> getPodcast() {
        return Optional.ofNullable(model.getPodcast())
            .filter(PodcastPromotable.class::isInstance)
            .map(PodcastPromotable.class::cast)
            .map(PodcastPromo::fromPromotable) //TODO should be PodcastPromoModule
            .map(promo -> createViews(PodcastEpisodePageViewPodcastField.class, promo))
            .orElse(null);
    }

    @Override
    public Iterable<? extends PodcastEpisodePageViewPrimaryAudioField> getPrimaryAudio() {
        return Optional.ofNullable(model)
                .map(AudioPodcastEpisodePage::getPrimaryAudio)
                .map(audio -> createViews(PodcastEpisodePageViewPrimaryAudioField.class, audio))
                .orElse(null);
    }

    @Override
    public Iterable<? extends PodcastEpisodePageViewSubHeadlineField> getSubHeadline() {
        return RichTextUtils.buildInlineHtml(
                model,
                AudioPodcastEpisodePage::getDescription,
                e -> createView(PodcastEpisodePageViewSubHeadlineField.class, e));
    }
}
