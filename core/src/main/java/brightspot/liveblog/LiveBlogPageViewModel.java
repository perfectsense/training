package brightspot.liveblog;

import java.time.Instant;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import brightspot.author.AuthoringPageViewModel;
import brightspot.l10n.CurrentLocale;
import brightspot.liveblog.wyntk.WhatYouNeedToKnowOption;
import brightspot.page.AbstractContentPageViewModel;
import brightspot.page.CurrentPageViewModel;
import brightspot.page.PageViewModel;
import brightspot.permalink.Permalink;
import brightspot.update.LastUpdatedProvider;
import brightspot.util.DateTimeUtils;
import brightspot.util.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.page.CurrentSite;
import com.psddev.cms.view.PageEntryView;
import com.psddev.cms.view.jsonld.JsonLdNode;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.web.annotation.WebParameter;
import com.psddev.styleguide.liveblog.LiveBlogPageView;
import com.psddev.styleguide.liveblog.LiveBlogPageViewBlogBodyField;
import com.psddev.styleguide.liveblog.LiveBlogPageViewBlogLeadField;
import com.psddev.styleguide.liveblog.LiveBlogPageViewCurrentPostsField;
import com.psddev.styleguide.liveblog.LiveBlogPageViewPinnedPostsField;
import com.psddev.styleguide.liveblog.LiveBlogPageViewWhatYouNeedToKnowField;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorBiographyField;
import com.psddev.styleguide.page.CreativeWorkPageViewAuthorNameField;
import com.psddev.styleguide.page.CreativeWorkPageViewContributorsField;
import com.psddev.styleguide.page.CreativeWorkPageViewHeadlineField;
import com.psddev.styleguide.page.CreativeWorkPageViewPeopleField;
import com.psddev.styleguide.page.CreativeWorkPageViewSubHeadlineField;
import com.psddev.styleguide.page.PageViewPageSubHeadingField;

public class LiveBlogPageViewModel extends AbstractContentPageViewModel<LiveBlog> implements
    LiveBlogPageView, PageEntryView {

    private static final String DATE_FORMAT_KEY = "dateFormat";

    @CurrentSite
    protected Site site;

    @WebParameter("since")
    protected Long since;

    @CurrentPageViewModel(AuthoringPageViewModel.class)
    protected AuthoringPageViewModel authoringPage;

    @CurrentLocale
    protected Locale locale;

    @Override
    public Iterable<? extends LiveBlogPageViewBlogBodyField> getBlogBody() {

        return RichTextUtils.buildHtml(
            model,
            LiveBlog::getBody,
            e -> createView(LiveBlogPageViewBlogBodyField.class, e));
    }

    @Override
    public Iterable<? extends LiveBlogPageViewBlogLeadField> getBlogLead() {

        return createViews(LiveBlogPageViewBlogLeadField.class, model.getLead());
    }

    @Override
    public Iterable<? extends LiveBlogPageViewCurrentPostsField> getCurrentPosts() {

        Date sinceDate = since == null ? null : new Date(since);

        return createViews(LiveBlogPageViewCurrentPostsField.class,
                model.getLivePosts(sinceDate, false, !isFragment(), model.getCurrentPostsCount()));
    }

    public boolean isFragment() {
        return since != null;
    }

    @Override
    public Boolean getFragment() {
        return isFragment();
    }

    @Override
    public Boolean getLiveEvent() {

        return model.isLiveEvent(site);
    }

    @Override
    public Iterable<? extends LiveBlogPageViewPinnedPostsField> getPinnedPosts() {

        return createViews(LiveBlogPageViewPinnedPostsField.class, model.getPinnedPosts());
    }

    @Override
    public CharSequence getPostsEndpoint() {
        return Permalink.getPermalink(site, model);
    }

    @Override
    public Iterable<? extends LiveBlogPageViewWhatYouNeedToKnowField> getWhatYouNeedToKnow() {

        return Optional.ofNullable(model.getWhatYouNeedToKnow())
            .map(WhatYouNeedToKnowOption::getWhatYouNeedToKnowText)
            .map(text -> RichTextUtils.buildHtml(model, m -> text,
                    e -> createView(LiveBlogPageViewWhatYouNeedToKnowField.class, e)))
            .orElse(null);
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewAuthorBiographyField> getAuthorBiography() {
        return authoringPage.getAuthorBiography(CreativeWorkPageViewAuthorBiographyField.class);
    }

    @Override
    public Map<String, ?> getAuthorImage() {
        return authoringPage.getAuthorImageAttributes();
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewAuthorNameField> getAuthorName() {
        return authoringPage.getAuthorName(CreativeWorkPageViewAuthorNameField.class);
    }

    @Override
    public CharSequence getAuthorUrl() {
        return authoringPage.getAuthorUrl();
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewContributorsField> getContributors() {
        return authoringPage.getContributors(CreativeWorkPageViewContributorsField.class);
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewPeopleField> getPeople() {
        return authoringPage.getAuthors(CreativeWorkPageViewPeopleField.class);
    }

    @JsonLdNode
    @Override
    public CharSequence getDateModified() {
        // Plain text
        return DateTimeUtils.format(
                LastUpdatedProvider.getMostRecentUpdateDate(model), LiveBlogPageView.class,
                DATE_FORMAT_KEY, page.getSite(), locale, PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @JsonLdNode("dateModified")
    @Override
    public CharSequence getDateModifiedISO() {
        return Optional.ofNullable(ObjectUtils.firstNonNull(LastUpdatedProvider.getMostRecentUpdateDate(model), model.getPublishDate()))
                .map(Date::toInstant)
                .map(Instant::toString)
                .orElse(null);
    }

    @JsonLdNode
    @Override
    public CharSequence getDatePublished() {
        // Plain text
        return DateTimeUtils.format(model.getPublishDate(), LiveBlogPageView.class, DATE_FORMAT_KEY, page.getSite(),
                locale, PageViewModel.DEFAULT_DATE_FORMAT
        );
    }

    @JsonLdNode("datePublished")
    @Override
    public CharSequence getDatePublishedISO() {
        return Optional.ofNullable(model.getPublishDate())
                .map(Date::toInstant)
                .map(Instant::toString)
                .orElse(null);
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewHeadlineField> getHeadline() {
        return RichTextUtils.buildInlineHtml(model, LiveBlog::getHeadline,
                e -> createView(CreativeWorkPageViewHeadlineField.class, e));
    }

    @Override
    public Iterable<? extends CreativeWorkPageViewSubHeadlineField> getSubHeadline() {

        if (model.isHideDescription()) {

            return null;
        }

        return RichTextUtils.buildInlineHtml(
            model,
            LiveBlog::getSubheadline,
            e -> createView(CreativeWorkPageViewSubHeadlineField.class, e));
    }

    @Override
    public Iterable<? extends PageViewPageSubHeadingField> getPageSubHeading() {
        // TODO: this shouldn't be on live blog page
        return null;
    }

    @Override
    public CharSequence getSource() {

        // TODO need HasSource model 2021-05-18
        return null;
    }
}
