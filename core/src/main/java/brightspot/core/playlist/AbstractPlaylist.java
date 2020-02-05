package brightspot.core.playlist;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import brightspot.core.image.ImageOption;
import brightspot.core.link.Linkable;
import brightspot.core.listmodule.ItemStreamContainer;
import brightspot.core.page.Page;
import brightspot.core.permalink.AbstractPermalinkRule;
import brightspot.core.pkg.Packageable;
import brightspot.core.promo.Promotable;
import brightspot.core.readreceipt.Readable;
import brightspot.core.section.SectionPrefixPermalinkRule;
import brightspot.core.section.Sectionable;
import brightspot.core.share.Shareable;
import brightspot.core.tag.Taggable;
import brightspot.core.timed.DurationUtils;
import brightspot.core.timed.TimedContent;
import brightspot.core.timedcontentitemstream.TimedContentItemStream;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;

/**
 * A list of audio and video files, or other {@linkplain TimedContent "timed content"}.
 */
@ToolUi.Main
@Seo.TitleFields("getSeoTitle")
@Seo.DescriptionFields("getSeoDescription")
public abstract class AbstractPlaylist extends Content implements
    Directory.Item,
    ItemStreamContainer,
    Linkable,
    Packageable,
    Page,
    Promotable,
    Readable,
    Sectionable,
    Shareable,
    Taggable {

    public static final String PROMOTABLE_TYPE = "playlist";

    private String name;

    @Indexed
    @Required
    @DisplayName("Items")
    private PlaylistItemStream itemStream = PlaylistItemStream.createDefault();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public PlaylistItemStream getItemStream() {
        return itemStream;
    }

    public void setItemStream(PlaylistItemStream itemStream) {
        this.itemStream = itemStream;
    }

    @Indexed
    @ToolUi.Hidden
    public List<TimedContent> getItems() {
        return Optional.ofNullable(getItemStream())
            .map(TimedContentItemStream::getTimedContent)
            .orElse(Collections.emptyList());
    }

    @Override
    public String getPromotableTitle() {
        return getName();
    }

    @Override
    public String getPromotableDescription() {
        return null;
    }

    @Override
    public ImageOption getPromotableImage() {
        return null;
    }

    @Override
    public String getPromotableType() {
        return PROMOTABLE_TYPE;
    }

    @Override
    public String getPromotableDuration() {
        return DurationUtils.durationToLabel(Duration.ofMillis(Optional.ofNullable(getItemStream())
            .map(PlaylistItemStream::getTimedContentItemStreamDuration)
            .orElse(0L)));
    }

    @Override
    public String getLinkableText() {
        return getPromotableTitle();
    }

    /**
     * The ignored/hidden technique is used here so that this method can be referenced via the {@code Seo.TitleFields}
     * annotation.
     */
    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoTitle() {
        return getPromotableTitle();
    }

    /**
     * The ignored/hidden technique is used here so that this method can be referenced via the {@code
     * Seo.DescriptionFields} annotation.
     */
    @Ignored(false)
    @ToolUi.Hidden
    public String getSeoDescription() {
        return null;
    }

    @Override
    public String createPermalink(Site site) {
        return AbstractPermalinkRule.create(site, this, SectionPrefixPermalinkRule.class);
    }
}
