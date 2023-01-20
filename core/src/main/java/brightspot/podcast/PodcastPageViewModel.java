package brightspot.podcast;

import java.util.Optional;

import brightspot.page.AbstractPageViewModel;
import brightspot.podcast.providers.HasPodcastProvidersMetadata;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.PageEntryView;
import com.psddev.dari.html.enumerated.LinkRel;
import com.psddev.styleguide.RawHtml;
import com.psddev.styleguide.page.PageViewMainField;
import com.psddev.styleguide.page.PageViewPageHeadingField;
import com.psddev.styleguide.page.PageViewPageLeadField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;
import com.psddev.styleguide.podcast.PodcastPageView;
import com.psddev.styleguide.podcast.PodcastPageViewBodyField;
import com.psddev.styleguide.podcast.PodcastPageViewHeadlineField;
import com.psddev.styleguide.podcast.PodcastPageViewImageField;
import com.psddev.styleguide.podcast.PodcastPageViewProvidersField;
import com.psddev.styleguide.podcast.PodcastPageViewSubHeadlineField;
import org.jaudiotagger.audio.asf.util.Utils;

import static com.psddev.dari.html.Nodes.*;

public class PodcastPageViewModel extends AbstractPageViewModel<PodcastPage> implements PageEntryView, PodcastPageView {

    @CurrentSite
    private Site site;

    @Override
    public Iterable<? extends PageViewPageHeadingField> getPageHeading() {
        // TODO this shouldn't even be present on PodcastPageView
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageLeadField> getPageLead() {
        // TODO this shouldn't even be present on PodcastPageView
        return null;
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        // TODO this shouldn't even be present on PodcastPageView
        return null;
    }

    @Override
    public Iterable<? extends PodcastPageViewHeadlineField> getHeadline() {
        return RichTextUtils.buildInlineHtml(
            model,
            PodcastPage::getTitle,
            e -> createView(PodcastPageViewHeadlineField.class, e));
    }

    @Override
    public Iterable<? extends PodcastPageViewSubHeadlineField> getSubHeadline() {
        return RichTextUtils.buildInlineHtml(
            model,
            PodcastPage::getDescription,
            e -> createView(PodcastPageViewSubHeadlineField.class, e));
    }

    @Override
    public Iterable<? extends PodcastPageViewImageField> getImage() {
        return Optional.ofNullable(model)
            .map(PodcastPage::getCoverArt)
            .map(coverArt -> createViews(PodcastPageViewImageField.class, coverArt))
            .orElse(null);
    }

    @Override
    public Iterable<? extends PodcastPageViewProvidersField> getProviders() {
        return createViews(
            PodcastPageViewProvidersField.class,
            model.as(HasPodcastProvidersMetadata.class));
    }

    @Override
    public Iterable<? extends PodcastPageViewBodyField> getBody() {
        return RichTextUtils.buildHtml(
            model, PodcastPage::getBody,
            e -> createView(PodcastPageViewBodyField.class, e));
    }

    @Override
    public Iterable<? extends PageViewMainField> getMain() {
        return createViews(PageViewMainField.class, model.getContents());
    }

    @Override
    public CharSequence getFeedLink() {

        return Optional.ofNullable(model)
                .map(podcast -> {
                    String feedUrl = podcast.getRssFeedUrl(site);
                    String feedTitle = podcast.getRssFeedTitle();

                    if (Utils.isBlank(feedUrl) || Utils.isBlank(feedTitle)) {
                        return null;
                    } else {

                        return RawHtml.of(LINK.rel(LinkRel.ALTERNATE)
                                .type("application/rss+xml")
                                .title(feedTitle)
                                .href(feedUrl)
                                .toString());

                    }
                })
                .orElse(null);
    }
}

