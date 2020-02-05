package brightspot.core.video;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.core.asset.Asset;
import brightspot.core.creativework.CreativeWork;
import brightspot.core.image.ImageOption;
import brightspot.core.image.OneOffImageOption;
import brightspot.core.lead.LeadItem;
import brightspot.core.link.Linkable;
import brightspot.core.page.Page;
import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.pkg.Packageable;
import brightspot.core.promo.PromotableWithOverrides;
import brightspot.core.readreceipt.Readable;
import brightspot.core.section.Section;
import brightspot.core.section.SectionPrefixPermalinkRule;
import brightspot.core.section.Sectionable;
import brightspot.core.share.Shareable;
import brightspot.core.site.ExpressSiteMapItem;
import brightspot.core.tag.Tag;
import brightspot.core.tag.Taggable;
import brightspot.core.timed.DurationUtils;
import brightspot.core.timed.TimedContent;
import brightspot.core.timed.TimedContentPlayerNoteRenderer;
import brightspot.core.timed.TimedContentToolPlayer;
import brightspot.core.update.LastUpdatedProvider;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Localization;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.HtmlWriter;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.sitemap.SiteMapEntry;
import com.psddev.sitemap.SiteMapSettingsModification;
import com.psddev.sitemap.SiteMapVideo;
import com.psddev.sitemap.VideoSiteMapItem;

@Recordable.PreviewField("getPreviewStorageItem")
@Seo.OpenGraphType("video")
@ToolUi.FieldDisplayOrder({
    "videoProvider",
    "headline",
    "subHeadline",
    "preview",
    "sectionable.section",
    "packageable.pkg",
    "taggable.tags",
    "taggable.tagging",
    // Main Tab
    "thumbnailOption",
    "promotable.promoTitle",
    "promotable.promoDescription",
    "promotable.promoImage",
    "shareable.shareTitle",
    "shareable.shareDescription",
    "shareable.shareImage",
    "page.layoutOption.layoutOption" }) // Overrides Tab
@ToolUi.FieldDisplayPreview({
    "promotable.promoImage",
    "headline",
    "subheadline",
    "taggable.tags",
    "cms.content.updateDate",
    "cms.content.updateUser"})
@ToolUi.IconName("slideshow")
@ToolUi.SearchShortcut(shortcut = "hl", field = "getHeadline")
@ToolUi.SearchShortcut(shortcut = "sh", field = "getSubHeadline")
public class Video extends CreativeWork implements
    Asset,
    Directory.Item,
    ExpressSiteMapItem,
    LeadItem,
    Linkable,
    Packageable,
    Page,
    PromotableWithOverrides,
    Readable,
    Sectionable,
    Shareable,
    Taggable,
    TimedContent,
    VideoMetaData,
    VideoPageElements,
    VideoSiteMapItem {

    public static final String ICON_NAME = "ondemand_video";
    public static final String TYPE = "video";

    public static final String VIDEO_PROVIDER_ID_FIELD = "getVideoProviderId";

    @Indexed
    @DisplayName("Provider")
    @ToolUi.Placeholder(dynamicText = "${content.getProviderMessageLabel()}")
    @ToolUi.NoteHtml("<span data-dynamic-html=\"${content.getDuplicateVideoNoteHtml(toolPageContext)}\"></span>")
    private VideoProvider videoProvider;

    @DisplayName("Provider")
    @Indexed
    @ToolUi.Hidden
    @ToolUi.Filterable
    @ToolUi.DropDown
    @Where("groups = " + VideoProvider.INTERNAL_NAME
        + " && internalName != " + VideoProvider.INTERNAL_NAME
        + " && (cms.ui.hidden = false || cms.ui.hidden = missing)"
        + " && isAbstract = false")
    private ObjectType videoProviderType;

    @ToolUi.NoteHtml("<span data-dynamic-html=\"${content.getPreviewNoteHtml(toolPageContext)}\"></span>")
    private String preview;

    @ToolUi.Tab("Overrides")
    @DisplayName("Thumbnail")
    private ImageOption thumbnailOption;

    @ToolUi.Tab(METADATA_TAB)
    @ToolUi.DisplayName("Dimensions")
    @ToolUi.Placeholder(dynamicText = "${content.getDimensionsLabel()}")
    @ToolUi.ReadOnly
    private String dimensionsLabel;

    @ToolUi.Tab(METADATA_TAB)
    @ToolUi.DisplayName("Aspect Ratio")
    @ToolUi.Placeholder(dynamicText = "${content.getAspectRatioLabel()}")
    @ToolUi.ReadOnly
    private String aspectRatioLabel;

    @ToolUi.Tab(METADATA_TAB)
    @ToolUi.DisplayName("Duration")
    @ToolUi.Placeholder(dynamicText = "${content.getDurationLabel()}")
    @ToolUi.ReadOnly
    private String durationLabel;

    // TODO: Is this still needed?
    @ToolUi.Hidden
    private Set<Option> options;

    public VideoProvider getVideoProvider() {
        updateVideoProvider();
        return videoProvider;
    }

    @Indexed
    @ToolUi.Hidden
    public String getVideoProviderId() {
        VideoProvider provider = getVideoProvider();
        return provider != null ? provider.getVideoId() : null;
    }

    @DisplayName("Video Provider ID")
    @Indexed
    @ToolUi.Hidden
    public String getUniqueIndex() {
        VideoProvider provider = getVideoProvider();
        String providerId = getVideoProviderId();

        if (provider != null && providerId != null) {
            return provider.getState().getType().getId().toString() + "-" + providerId;
        } else {
            return null;
        }
    }

    public void setVideoProvider(VideoProvider videoProvider) {
        this.videoProvider = videoProvider;
    }

    /**
     * Not for public use.
     */
    @Override
    public String getHeadlineFallback() {
        VideoProvider videoProvider = getVideoProvider();
        return videoProvider != null ? videoProvider.getVideoTitleFallback() : null;
    }

    /**
     * Not for public use.
     */
    @Override
    public String getSubHeadlineFallback() {
        VideoProvider videoProvider = getVideoProvider();
        return videoProvider != null ? videoProvider.getVideoDescriptionFallback() : null;
    }

    public ImageOption getThumbnailOption() {
        return thumbnailOption;
    }

    public void setThumbnailOption(ImageOption thumbnailOption) {
        this.thumbnailOption = thumbnailOption;
    }

    @Override
    public ImageOption getThumbnail() {
        ImageOption thumbnail = thumbnailOption;

        if (thumbnail == null) {
            VideoProvider videoProvider = getVideoProvider();

            if (videoProvider != null) {
                StorageItem providerThumbnailFallback = videoProvider.getVideoThumbnailFallback();

                if (providerThumbnailFallback != null) {
                    OneOffImageOption oneOff = new OneOffImageOption();
                    oneOff.setFile(providerThumbnailFallback);
                    oneOff.setAltText(getHeadline());
                    thumbnail = oneOff;
                }
            }
        }

        return thumbnail;
    }

    public String getDimensionsLabel() {
        Dimensions dimensions = getVideoProviderDimensions();
        return dimensions != null ? dimensions.getDimensionsLabel() : null;
    }

    public String getAspectRatioLabel() {
        Dimensions dimensions = getVideoProviderDimensions();
        return dimensions != null ? dimensions.getAspectRatioLabel() : null;
    }

    public String getDurationLabel() {
        return DurationUtils.durationToLabel(getVideoProviderDuration());
    }

    @Override
    public Set<Option> getOptions() {
        if (options == null) {
            options = new LinkedHashSet<>();
        }
        return options;
    }

    public void setOptions(Set<Option> options) {
        this.options = options;
    }

    // Used by @Recordable.PreviewField annotation
    @Ignored(false)
    @ToolUi.Hidden
    @Override
    public StorageItem getPreviewStorageItem() {
        ImageOption thumbnail = getThumbnail();
        return thumbnail != null ? thumbnail.getImageOptionFile() : null;
    }

    public String getPreviewNoteHtml(ToolPageContext page) {
        return new TimedContentPlayerNoteRenderer()
            .render(this, getState().getField("preview"), page);
    }

    public String getDuplicateVideoNoteHtml(ToolPageContext page) {
        if (videoProvider == null) {
            return null;
        }

        Video duplicateVideo = Query.from(Video.class)
            .where("getUniqueIndex = ?", getUniqueIndex())
            .and("_id != ?", getId())
            .first();

        if (duplicateVideo == null) {
            return null;
        }

        try {
            StringWriter stringWriter = new StringWriter();
            HtmlWriter htmlWriter = new HtmlWriter(stringWriter);

            htmlWriter.writeHtml(Localization.currentUserText(
                this,
                "message.videoDuplicate",
                "This video already exists."));
            htmlWriter.writeStart(
                "a",
                "href",
                page.cmsUrl("/content/edit.jsp", "id", duplicateVideo.getId()),
                "class",
                "objectId-edit",
                "target",
                "objectId-1-edit")
                .writeHtml(Localization.currentUserText(getClass(), "action.edit"))
                .writeEnd();

            return stringWriter.toString();
        } catch (IOException ex) {
            // Ignore
        }

        return null;
    }

    @Indexed
    @ToolUi.Hidden
    public Long getDuration() {
        VideoProvider provider = getVideoProvider();
        return provider != null ? provider.getVideoDuration() : null;
    }

    private void updateVideoProvider() {
        if (videoProvider != null) {
            videoProvider.setVideoMetaData(this);
            videoProviderType = videoProvider.getState().getType();
        } else {
            videoProviderType = null;
        }
    }

    @Override
    public void beforeSave() {
        super.beforeSave();

        updateVideoProvider();
    }

    @Override
    public boolean shouldValidate(ObjectField field) {
        // skip validation of this field's where clause since it's just for global search purposes.
        return !"videoProviderType".equals(field.getInternalName())
            && super.shouldValidate(field);
    }

    @Override
    public void onValidate() {
        super.onValidate();

        if (videoProvider == null) {
            getState().addError(getState().getField("videoProvider"), "Required!");
        }
    }

    @Override
    public String getLabel() {
        return getHeadline();
    }

    @Override
    public ImageOption getLeadItemImage() {
        return getThumbnail();
    }

    // ExpressSiteMapItem Support

    @Override
    public List<SiteMapEntry> getSiteMapEntries() {
        List<SiteMapEntry> siteMapEntries = new ArrayList<>();

        Stream.concat(Site.Static.findAll().stream(), Stream.of(new Site[] { null })).forEach(site -> {

            String sitePermalinkPath = as(Directory.ObjectModification.class).getSitePermalinkPath(site);
            if (sitePermalinkPath == null) {
                return;
            }

            VideoProvider videoProvider = this.getVideoProvider();
            if (videoProvider == null) {
                return;
            }

            String thumbnailLoc = Optional.ofNullable(this.getThumbnail())
                .map(ImageOption::getImageOptionFile)
                .map(StorageItem::getPublicUrl)
                .orElse(null);
            if (thumbnailLoc == null) {
                return;
            }

            SiteMapEntry siteMapEntry = new SiteMapEntry();
            siteMapEntry.setUpdateDate(
                ObjectUtils.firstNonNull(
                    LastUpdatedProvider.getMostRecentUpdateDate(getState()),
                    getState().as(Content.ObjectModification.class).getPublishDate()
                )
            );

            siteMapEntry.setPermalink(SiteSettings.get(
                site,
                f -> f.as(SiteMapSettingsModification.class).getSiteMapDefaultUrl() + StringUtils.ensureStart(
                    sitePermalinkPath,
                    "/")));

            SiteMapVideo siteMapVideo = new SiteMapVideo();

            siteMapVideo.setThumbnailLoc(thumbnailLoc);

            siteMapVideo.setTitle(ObjectUtils.firstNonBlank(
                this.as(Seo.ObjectModification.class).getTitle(),
                this.getHeadline()
            ));

            siteMapVideo.setDescription(ObjectUtils.firstNonBlank(
                this.as(Seo.ObjectModification.class).getDescription(),
                this.getSubHeadline(),
                this.getHeadline()));
            siteMapVideo.setContentLoc(videoProvider.getEmbeddableStreamUrl());

            Long duration = this.getDuration();
            if (duration != null) {
                siteMapVideo.setDuration(duration / 1000);
            }

            String category = Optional.ofNullable(this.asSectionableData().getSection())
                .map(Section::getDisplayName)
                .orElse(null);

            if (category != null) {
                siteMapVideo.setCategory(category);
            }

            List<String> tags = this.asTaggableData().getTags().stream()
                .map(Tag::getDisplayName)
                .collect(Collectors.toList());

            if (tags.size() < 10) {
                tags.addAll(this.as(Seo.ObjectModification.class).getKeywords());
            }

            if (!ObjectUtils.isBlank(tags)) {
                if (tags.size() > 10) {
                    tags = tags.subList(0, 10);
                }
                siteMapVideo.setTags(tags);
            }

            siteMapEntry.setVideos(Arrays.asList(siteMapVideo));

            siteMapEntries.add(siteMapEntry);
        });

        return siteMapEntries;
    }

    // TimedContent Support

    @Override
    public Long getTimedContentDuration() {
        Duration duration = getVideoProviderDuration();
        return duration != null ? duration.toMillis() : null;
    }

    @Override
    public TimedContentToolPlayer getTimedContentToolPlayer() {
        return new VideoToolPlayer(this);
    }

    // Promotable Support

    @Override
    public ImageOption getPromotableImageFallback() {
        return getThumbnail();
    }

    @Override
    public String getPromotableTitleFallback() {
        return getHeadline();
    }

    @Override
    public String getPromotableDescriptionFallback() {
        return getSubHeadline();
    }

    @Override
    public String getPromotableType() {
        return TYPE;
    }

    @Override
    public String getPromotableDuration() {
        return getDurationLabel();
    }

    // Shareable Support

    @Override
    public String getShareableTitleFallback() {
        return getPromotableTitleFallback();
    }

    @Override
    public String getShareableDescriptionFallback() {
        return getPromotableDescriptionFallback();
    }

    @Override
    public ImageOption getShareableImageFallback() {
        return getPromotableImageFallback();
    }

    private Dimensions getVideoProviderDimensions() {
        VideoProvider provider = getVideoProvider();

        if (provider != null) {
            return Dimensions.of(provider.getOriginalVideoWidth(), provider.getOriginalVideoHeight());
        }

        return null;
    }

    private Duration getVideoProviderDuration() {
        VideoProvider provider = getVideoProvider();

        if (provider != null) {
            Long duration = provider.getVideoDuration();

            if (duration != null) {
                return Duration.ofMillis(duration);
            }
        }

        return null;
    }

    @Override
    public String getLinkableText() {
        return getPromotableTitle();
    }

    @Override
    public TimedContent getTimedContentItemContent() {
        return this;
    }

    @Override
    public Long getTimedContentItemDuration() {
        return getDuration();
    }

    public String getProviderMessageLabel() {
        return Localization.currentUserText(Video.class, "message.selectProvider", "Select Provider...");
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, SectionPrefixPermalinkRule.class);
    }
}
