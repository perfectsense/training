package brightspot.core.playlist;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import brightspot.core.timed.DurationUtils;
import brightspot.core.timed.TimedContent;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.State;

class PlaylistItemsNoteRenderer implements ToolUi.NoteRenderer {

    @Override
    public String render(Object object) {

        List<PlaylistItem> items = null;
        long duration = 0;

        if (object instanceof PlaylistItemList) {

            items = ((PlaylistItemList) object).getItems();
            duration = ((PlaylistItemList) object).getDuration();
        }

        if (items == null) {
            return null;
        }

        Map<ObjectType, Integer> itemTypeCounts = new TreeMap<>();

        List<ObjectType> itemTypes = items.stream()
            .map(PlaylistItem::getTimedContent)
            .filter(Objects::nonNull)
            .map(TimedContent::getState)
            .map(State::getType)
            .collect(Collectors.toList());

        for (ObjectType itemType : itemTypes) {

            Integer count = itemTypeCounts.get(itemType);

            if (count == null) {
                count = 1;
            } else {
                count += 1;
            }

            itemTypeCounts.put(itemType, count);
        }

        StringBuilder label = new StringBuilder();

        label.append("Duration: ");
        label.append(DurationUtils.durationToLabel(Duration.ofMillis(duration)));

        if (!itemTypeCounts.isEmpty()) {

            for (Map.Entry<ObjectType, Integer> entry : itemTypeCounts.entrySet()) {
                label.append(" | ");
                label.append(entry.getKey().getLabel());
                label.append(" Count: ");
                label.append(entry.getValue());
            }
        }

        return label.toString();
    }
}
