package brightspot.liveblog;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import brightspot.article.ArticleLead;
import brightspot.author.HasAuthorsWithField;
import brightspot.embargo.Embargoable;
import brightspot.image.WebImageAsset;
import brightspot.liveblog.wyntk.WhatYouNeedToKnowOption;
import brightspot.page.Page;
import brightspot.permalink.AbstractPermalinkRule;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.rss.DynamicFeedSource;
import brightspot.rte.LargeRichTextToolbar;
import brightspot.rte.SmallRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.search.modifier.exclusion.SearchExcludable;
import brightspot.section.HasSectionWithField;
import brightspot.seo.SeoWithFields;
import brightspot.share.Shareable;
import brightspot.site.DefaultSiteMapItem;
import brightspot.tag.HasTagsWithField;
import brightspot.urlslug.HasUrlSlugWithField;
import brightspot.util.RichTextUtils;
import brightspot.util.Truncate;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.Utils;
import com.psddev.feed.FeedItem;
import org.apache.commons.lang3.StringUtils;

@ToolUi.FieldDisplayOrder({
        "headline",
        "subheadline",
        "hasUrlSlug.urlSlug",
        "hasAuthorsWithField.authors",
        "lead",
        "whatYouNeedToKnow",
        "body",
        "hasSectionWithField.section",
        "hasTags.tags",
        "embargoable.embargo"
})
@ToolUi.IconName("question_answer")
public class LiveBlog extends Content implements
        DefaultSiteMapItem,
        DynamicFeedSource,
        Embargoable,
        FeedItem,
        HasAuthorsWithField,
        HasSectionWithField,
        HasTagsWithField,
        HasUrlSlugWithField,
        ILiveBlog<LiveBlogPost>,
        Page,
        PagePromotableWithOverrides,
        SearchExcludable,
        SeoWithFields,
        Shareable {

    private static final String TYPE = "live-blog";

    private static final int DEFAULT_CURRENT_POSTS_COUNT = 1000;

    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String headline;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String subheadline;

    @ToolUi.CssClass("is-minimal")
    @ToolUi.Tab("Overrides")
    private Boolean hideDescription;

    private WhatYouNeedToKnowOption whatYouNeedToKnow;

    @ToolUi.Tab("Overrides")
    @ToolUi.Placeholder(value = DEFAULT_CURRENT_POSTS_COUNT + "", editable = true)
    private Integer currentPostsCount;

    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class, lines = 10)
    private String body;

    private ArticleLead lead;

    @Required
    @Embedded
    private LiveEventOption liveEvent = new AutomaticLiveEventOption();

    @ToolUi.Hidden
    private List<LiveBlogPost> pinnedPosts;

    public String getHeadline() {

        return headline;
    }

    public void setHeadline(String headline) {

        this.headline = headline;
    }

    public String getSubheadline() {

        return subheadline;
    }

    public void setSubheadline(String subheadline) {

        this.subheadline = subheadline;
    }

    public boolean isHideDescription() {
        return Boolean.TRUE.equals(hideDescription);
    }

    public void setHideDescription(Boolean hideDescription) {
        this.hideDescription = Boolean.TRUE.equals(hideDescription) ? true : null;
    }

    public WhatYouNeedToKnowOption getWhatYouNeedToKnow() {

        return whatYouNeedToKnow;
    }

    public void setWhatYouNeedToKnow(WhatYouNeedToKnowOption whatYouNeedToKnow) {

        this.whatYouNeedToKnow = whatYouNeedToKnow;
    }

    public ArticleLead getLead() {

        return lead;
    }

    public void setLead(ArticleLead lead) {

        this.lead = lead;
    }

    public String getBody() {

        return body;
    }

    public void setBody(String body) {

        this.body = body;
    }

    public LiveEventOption getLiveEvent() {

        return liveEvent;
    }

    public void setLiveEvent(LiveEventOption liveEvent) {

        this.liveEvent = liveEvent;
    }

    @Override
    public LiveBlogPost createPost() {
        LiveBlogPost post = new LiveBlogPost();
        post.setLiveBlog(this);
        return post;
    }

    @Override
    public Query<LiveBlogPost> getPostsQuery() {
        return Query.from(LiveBlogPost.class).where("liveBlog = ?", this);
    }

    @Override
    public List<LiveBlogPost> getPinnedPosts() {

        if (pinnedPosts == null) {

            pinnedPosts = new ArrayList<>();
        }

        return pinnedPosts;
    }

    @Override
    public void pinPost(UUID postId) {
        Object postObj = Query.fromAll().where("_id = ?", postId).first();
        if (postObj instanceof LiveBlogPost) {
            getPinnedPosts().add((LiveBlogPost) postObj);
        }
    }

    @Override
    public void unpinPost(UUID postId) {
        getPinnedPosts().removeIf(post -> postId.equals(post.getId()));
    }

    public void setPinnedPosts(List<LiveBlogPost> pinnedPosts) {

        this.pinnedPosts = pinnedPosts;
    }

    public List<LiveBlogPost> getLivePosts(
        Date since,
        boolean useUpdateDate,
        boolean sortDescending,
        Integer limit) {

        Query<LiveBlogPost> query = getLiveBlogPostsQuery(since, useUpdateDate, sortDescending);

        return limit != null
            ? query.select(0, limit).getItems()
            : query.selectAll();
    }

    public LiveBlogPost getLatestPost(
        boolean useUpdateDate) {

        return getLiveBlogPostsQuery(null, useUpdateDate, true)
            .first();
    }

    private Query<LiveBlogPost> getLiveBlogPostsQuery(
        Date since,
        boolean useUpdateDate,
        boolean sortDescending) {

        Query<LiveBlogPost> query = getPostsQuery();

        List<LiveBlogPost> pinnedPosts = getPinnedPosts();
        if (!ObjectUtils.isBlank(pinnedPosts)) {

            query.where(Query.ID_KEY + " != ?", getPinnedPosts());
        }

        String index = Content.PUBLISH_DATE_FIELD;
        if (useUpdateDate) {

            index = LiveBlogPost.GET_UPDATE_DATE_FIELD;
        }

        query.and(index + " != missing");

        if (since != null) {

            query.and(index + " > ?", since);
        }

        if (sortDescending) {

            query.sortDescending(index);
        } else {

            query.sortAscending(index);
        }

        return query;
    }

    public boolean isLiveEvent(Site site) {

        Instant now = Instant.now();

        // Get last update date.
        Date lastUpdate = Optional.ofNullable(getLatestPost(true))
            .map(LiveBlogPost::getUpdateDate)
            .orElseGet(this::getUpdateDate);
        if (lastUpdate == null) {

            return false;
        }

        return Optional.ofNullable(getLiveEvent())
            .map(liveEventOption -> liveEventOption.isLiveEvent(site, lastUpdate.toInstant(), now))
            .orElse(false);
    }

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {

        return AbstractPermalinkRule.create(site, this, LiveBlogPermalinkRule.class);
    }

    // --- DynamicFeedSource support ---

    @Override
    public List<FeedItem> getFeedItems(Site site) {

        return getLivePosts(null, false, true, null)
            .stream()
            .filter(liveBlogPost -> liveBlogPost.isInstantiableTo(FeedItem.class))
            .map(liveBlogPost -> liveBlogPost.as(FeedItem.class))
            .collect(Collectors.toList());
    }

    @Override
    public String getFeedLanguage(Site site) {

        return getDynamicFeedLanguage(site);
    }

    // --- FeedItem support ---

    @Override
    public String getFeedTitle() {

        return getPagePromotableTitle();
    }

    @Override
    public String getFeedDescription() {

        return getPagePromotableDescription();
    }

    @Override
    public String getFeedLink(Site site) {

        return getLinkableUrl(site);
    }

    // --- Linkable support ---

    @Override
    public String getLinkableText() {

        return getPagePromotableTitle();
    }

    // --- Promotable support ---

    @Override
    public String getPagePromotableType() {

        return TYPE;
    }

    @Override
    public String getPagePromotableTitleFallback() {

        return getHeadline();
    }

    @Override
    public String getPagePromotableDescriptionFallback() {

        String promotableDescriptionFallback = Optional.ofNullable(getSubheadline())
            .map(RichTextUtils::richTextToPlainText)
            .orElse(null);

        if (!StringUtils.isBlank(promotableDescriptionFallback)) {
            return promotableDescriptionFallback;
        }

        if (!StringUtils.isBlank(ObjectUtils.to(String.class, getState().get("migration.legacyId")))) {
            // only fall back to body text for migrated content
            return Optional.ofNullable(getBody())
                .map(RichTextUtils::stripRichTextElements)
                .map(RichTextUtils::richTextToPlainText)
                .map(text -> Truncate.truncate(text, 155, true))
                .orElse(null);
        }

        return null;
    }

    @Override
    public WebImageAsset getPagePromotableImageFallback() {

        return Optional.ofNullable(getLead())
            .map(ArticleLead::getArticleLeadImage)
            .orElse(null);
    }

    // --- SeoHooks support ---

    @Override
    public String getSeoTitleFallback() {
        return getHeadline();
    }

    @Override
    public String getSeoDescriptionFallback() {
        return getPagePromotableDescriptionFallback();
    }

    @Override
    public String getSeoTitle() {

        return getHeadline();
    }

    @Override
    public String getSeoDescription() {

        return getSubheadline();
    }

    // --- Shareable support ---

    @Override
    public String getShareableTitleFallback() {

        return getPagePromotableTitleFallback();
    }

    @Override
    public String getShareableDescriptionFallback() {

        return getPagePromotableDescriptionFallback();
    }

    @Override
    public WebImageAsset getShareableImageFallback() {

        return getPagePromotableImageFallback();
    }

    public int getCurrentPostsCount() {
        return currentPostsCount != null
                ? currentPostsCount
                : DEFAULT_CURRENT_POSTS_COUNT;
    }

    public void setCurrentPostsCount(Integer currentPostsCount) {
        this.currentPostsCount = currentPostsCount;
    }

    // --- Sluggable support ---

    @Override
    public String getUrlSlugFallback() {

        return Utils.toNormalized(getHeadline());
    }
}
