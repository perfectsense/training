package brightspot.liveblog;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import brightspot.actionbar.HasActionBar;
import brightspot.anchor.AnchorLinkable;
import brightspot.author.HasAuthorsWithField;
import brightspot.image.WebImageAsset;
import brightspot.permalink.Permalink;
import brightspot.promo.page.PagePromotableWithOverrides;
import brightspot.rte.LargeRichTextToolbar;
import brightspot.rte.TinyRichTextToolbar;
import brightspot.rte.image.ImageRichTextElement;
import brightspot.search.boost.HasSiteSearchBoostIndexes;
import brightspot.share.Shareable;
import brightspot.util.RichTextUtils;
import brightspot.util.Truncate;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Managed;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.rtc.RtcEvent;
import com.psddev.cms.tool.ContentEditPublishRedirectUrlSupplier;
import com.psddev.cms.tool.ContentEditWidgetDisplay;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.cms.tool.page.content.Edit;
import com.psddev.cms.ui.ToolRequest;
import com.psddev.dari.web.WebRequest;
import com.psddev.feed.FeedItem;
import org.apache.commons.lang3.StringUtils;

@ToolUi.ExcludeFromGlobalSearch
@ToolUi.CssClass("is-inGrid")
public class LiveBlogPost extends Content implements
        ILiveBlogPost,
        AnchorLinkable,
        ContentEditWidgetDisplay,
        ContentEditPublishRedirectUrlSupplier,
        FeedItem,
        HasActionBar,
        HasAuthorsWithField,
        HasSiteSearchBoostIndexes,
        Managed,
        RtcEvent,
        PagePromotableWithOverrides,
        Shareable {

    public static final String GET_UPDATE_DATE_FIELD = "getUpdateDate";

    @Indexed
    @Required
    @ToolUi.RichText(toolbar = TinyRichTextToolbar.class)
    private String headline;

    @Indexed
    @Required
    @ToolUi.RichText(inline = false, toolbar = LargeRichTextToolbar.class, lines = 10)
    private String body;

    @ToolUi.Tab("Overrides")
    private Date updateDateOverride;

    @Required
    @Indexed
    @ToolUi.Hidden
    private LiveBlog liveBlog;

    public String getHeadline() {

        return headline;
    }

    public void setHeadline(String headline) {

        this.headline = headline;
    }

    public String getBody() {

        return body;
    }

    public void setBody(String body) {

        this.body = body;
    }

    @Override
    @Indexed
    @ToolUi.Hidden
    public Date getUpdateDate() {

        return Optional.ofNullable(getUpdateDateOverride())
            .orElseGet(() -> as(ObjectModification.class).getUpdateDate());
    }

    public Date getUpdateDateOverride() {

        return updateDateOverride;
    }

    public void setUpdateDateOverride(Date updateDateOverride) {

        this.updateDateOverride = updateDateOverride;
    }

    @Override
    public LiveBlog getLiveBlog() {

        return liveBlog;
    }

    public void setLiveBlog(LiveBlog liveBlog) {

        this.liveBlog = liveBlog;
    }

    public String getPostUrl(Site site) {

        // Get anchor.
        String anchor = getAnchorLinkableAnchor();

        // Get live blog permalink.
        String liveBlogPermalink = Optional.ofNullable(getLiveBlog())
            .map(blog -> Permalink.getPermalink(site, blog))
            .orElse(null);
        if (liveBlogPermalink == null) {

            return null;
        }

        return liveBlogPermalink + "#" + anchor;
    }

    // --- Anchorable support ---

    @Override
    public String getAnchorLinkableAnchor() {

        return getId().toString();
    }

    // --- ContentEditWidgetDisplay support ---

    @Override
    public boolean shouldDisplayContentEditWidget(String widgetName) {

        return false;
    }

    // --- ContentEditPublishRedirectUrlSupplier support ---

    @Override
    public String getContentEditPublishRedirectUrl(ToolPageContext page) {

        LiveBlogPost post = new LiveBlogPost();

        String blogIdParam = Optional.ofNullable(getLiveBlog())
            .map(LiveBlog::getId)
            .map(UUID::toString)
            .orElseGet(() -> page.param(String.class, LiveBlogLivePostServlet.BLOG_ID_PARAM));

        if (getLiveBlog() == null && !StringUtils.isBlank(blogIdParam)) {

            LiveBlog blog = ILiveBlog.getById(LiveBlog.class, UUID.fromString(blogIdParam), true);
            post.setLiveBlog(blog);

        } else {

            post.setLiveBlog(getLiveBlog());
        }

        return page.dataUrl(
            Edit.PAGE_URL, post,
            LiveBlogLivePostServlet.MANAGED_OVERRIDE_PARAM, true,
            LiveBlogLivePostServlet.BLOG_ID_PARAM, blogIdParam);
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

        return getPostUrl(site);
    }

    // --- HasSiteSearchBoostIndexes support ---

    @Override
    public String getSiteSearchBoostTitle() {
        return getHeadline();
    }

    @Override
    public String getSiteSearchBoostDescription() {
        return getPagePromotableDescription();
    }

    // --- Linkable support ---

    @Override
    public String getLinkableText() {

        return getHeadline();
    }

    @Override
    public String getLinkableUrl(Site site) {

        return getPostUrl(site);
    }

    // --- Managed support ---

    @Override
    public String createManagedEditUrl(ToolPageContext page) {

        // If the override param is true and the liveblog is set then dont redirect
        if (liveBlog != null && WebRequest.getCurrent()
                .getParameter(boolean.class, LiveBlogLivePostServlet.MANAGED_OVERRIDE_PARAM)) {
            return null;
        } else if (liveBlog == null) {
            // this case will only trigger if someone attempts to "Create New" a LBP
            return page.objectUrl("/content/edit.jsp", new LiveBlog());
        }
        // Otherwise redirect to the liveblog post UI
        return WebRequest.getCurrent()
                .as(ToolRequest.class)
                .getPathBuilder(LiveBlogLivePostServlet.PATH)
                .setParameter(LiveBlogLivePostServlet.BLOG_ID_PARAM, liveBlog.getId())
                .setParameter(LiveBlogLivePostServlet.POST_ID_PARAM, getId())
                .build();
    }

    // --- RtcEvent support ---

    @Override
    public void onDisconnect() {

        // Do Nothing
    }

    // --- Promotable support ---

    @Override
    public String getPagePromotableTitleFallback() {

        return getHeadline();
    }

    @Override
    public String getPagePromotableDescriptionFallback() {

        return Optional.ofNullable(getBody())
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::richTextToPlainText)
            .map(text -> Truncate.truncate(text, 155, true))
            .orElse(null);
    }

    @Override
    public WebImageAsset getPagePromotableImageFallback() {

        if (!StringUtils.isBlank(body)) {

            return ImageRichTextElement.getFirstImageFromRichText(body);
        }

        return null;
    }

    // --- Shareable support ---

    @Override
    public String getShareableTitleFallback() {

        return getPagePromotableTitle();
    }

    @Override
    public String getShareableDescriptionFallback() {

        return getPagePromotableDescription();
    }

    @Override
    public WebImageAsset getShareableImageFallback() {

        return getPagePromotableImage();
    }

    @Override
    public String getLabel() {
        return RichTextUtils.richTextToPlainText(getHeadline());
    }
}
