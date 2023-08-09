package brightspot.rss;

import java.util.Set;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Alteration;
import com.psddev.dari.db.ObjectType;
import com.psddev.feed.FeedSource;

public class FeedSourceAlteration extends Alteration<FeedSource> {

    private static final String TAB = "Advanced";
    private static final String CLUSTER = "RSS Settings";

    @InternalName("feed.disableFeed")
    @ToolUi.Tab(TAB)
    @ToolUi.Cluster(CLUSTER)
    private boolean disableFeed;

    @InternalName("feed.renderFullContent")
    @ToolUi.Tab(TAB)
    @ToolUi.Cluster(CLUSTER)
    private boolean renderFullContent;

    @InternalName("feed.enabledFeedItemTypes")
    @ToolUi.Tab(TAB)
    @ToolUi.Cluster(CLUSTER)
    private Set<ObjectType> enabledFeedItemTypes;

}
