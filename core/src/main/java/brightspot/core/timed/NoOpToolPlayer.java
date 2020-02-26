package brightspot.core.timed;

import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.util.StorageItem;

/**
 * Placeholder player used for debugging purposes and doesn't actually do anything.
 */
public class NoOpToolPlayer implements TimedContentToolPlayer {

    @Override
    public StorageItem getTimedContentToolPlayerPreview(ToolPageContext page) {
        return StorageItem.Static.createUrl("https://placeholdit.imgix.net/~text?txtsize=33&txt=Thumbnail&w=350&h=250");
    }

    @Override
    public String getTimedContentPreviewFrameUrl(ToolPageContext page) {
        return NoOpToolPlayerServlet.PAGE_URL;
    }

    @Override
    public String getTimedCompanionSelectionFrameUrl(ToolPageContext page) {
        return NoOpToolPlayerServlet.PAGE_URL;
    }
}
