package brightspot.core.playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import brightspot.core.timed.TimedCompanion;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class PlaylistItemList extends Record {

    @Indexed
    @ToolUi.Unlabeled
    @ToolUi.NoteHtml("<span data-dynamic-html='${content.getItemsNoteHtml()}'></span>")
    private List<PlaylistItem> items;

    public List<PlaylistItem> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        updatePlaylistItems();
        return items;
    }

    public void setItems(List<PlaylistItem> items) {
        this.items = items;
    }

    /**
     * @return The duration (in milliseconds) of the playlist.
     */
    public long getDuration() {
        return getPlaylistItemDuration(items);
    }

    // in milliseconds
    private static long getPlaylistItemDuration(List<PlaylistItem> items) {
        return items == null ? 0 : items.stream()
            .map(PlaylistItem::getDuration)
            .filter(Objects::nonNull)
            .mapToLong(l -> l)
            .filter(duration -> duration >= 0)
            .sum();
    }

    private void updatePlaylistItems() {

        if (items == null) {
            return;
        }

        long totalDuration = getDuration();

        for (PlaylistItem item : items) {

            // Calculating weight
            Long itemDuration = item.getDuration();
            long duration = itemDuration != null ? itemDuration : 0;

            double weight = 1;
            if (totalDuration > 0) {
                weight = (double) duration / (double) totalDuration;
            }
            item.setWeight(weight);

            // Collecting event markers
            List<TimedCompanion> timedCompanions = item.getTimedCompanions();

            if (timedCompanions != null) {
                List<Double> eventMarkers = timedCompanions.stream()
                    .map(TimedCompanion::getOffsetMilliseconds)
                    .filter(Objects::nonNull)
                    .mapToLong(l -> l)
                    .filter(offset -> duration > 0 && offset >= 0 && offset < duration)
                    .mapToDouble(offset -> (double) offset / (double) duration)
                    .boxed()
                    .collect(Collectors.toList());

                item.setMarkers(eventMarkers);

            } else {
                item.setMarkers(null);
            }
        }
    }

    public String getItemsNoteHtml() {
        return new PlaylistItemsNoteRenderer().render(this);
    }

    @Override
    public void beforeSave() {
        super.beforeSave();
        updatePlaylistItems();
    }
}
