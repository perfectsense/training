package brightspot.core.timed;

import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.util.StorageItem;

/**
 * <p>
 * A player that can be loaded in the CMS tool to power editorial functionality around {@link TimedContent}.
 * </p><p>
 * For the {@linkplain #getTimedCompanionSelectionFrameUrl(ToolPageContext)} API, the resulting frame's Javascript needs
 * to broadcast, listen and respond to certain events. All events are targeted at the closest parent element matching
 * the selector {@code .TimedContentPlayerNote-player-frame}.
 * </p><p>
 * <b>timeUpdate.timed</b> - During playback, whenever the player has a time
 * update, it should broadcast the event 'timeUpdate.timed' passing the new current time as a Number in seconds.
 * </p><p>
 * <b>pause.timed</b> - The player should listen for 'pause.timed' events and
 * pause itself appropriately.
 * </p><p>
 * <b>seek.timed</b> - The player should listen for 'seek.timed' events and
 * seek to the time that is passed as a Number argument in seconds.
 * </p><p>
 * <b>destroy.timed</b> - The player should listen for 'destroy.timed' which
 * lets the player know that it is no longer needed and should clean up any of its resources before it is automatically
 * removed from the DOM.
 * </p><p>
 * See PlyrMediaToolPlayer.js as example implementation.
 * <p>
 */
public interface TimedContentToolPlayer {

    /**
     * @param page The current page context.
     * @return A StorageItem to display as a preview for the player.
     */
    StorageItem getTimedContentToolPlayerPreview(ToolPageContext page);

    /**
     * @param page The current page context.
     * @return The URL to the video player used for preview.
     */
    String getTimedContentPreviewFrameUrl(ToolPageContext page);

    /**
     * @param page The current page context.
     * @return The URL to the video player used for companion selection.
     */
    String getTimedCompanionSelectionFrameUrl(ToolPageContext page);
}
