package brightspot.core.playlist;

import java.util.List;

import brightspot.core.timed.CascadingTimedCompanionList;
import brightspot.core.timed.CascadingTimedCompanionListNoteRenderer;
import brightspot.core.timed.DurationUtils;
import brightspot.core.timed.TimedCompanion;
import brightspot.core.timed.TimedContent;
import brightspot.core.timed.TimedContentProvider;
import brightspot.core.timedcontentitemstream.TimedContentItem;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Recordable;

/**
 * An item within a {@linkplain Playlist}.
 */
@Recordable.Embedded
public class PlaylistItem extends Content implements
    TimedContentItem,
    TimedContentProvider {

    @Required
    @DisplayName("Item")
    @Indexed
    private TimedContent timedContent;

    @Embedded
    @ToolUi.Tab("Companion Content")
    @ToolUi.NoteHtml("<span data-dynamic-html='${content.getTimedCompanionListNoteHtml(toolPageContext)}'></span>")
    @ToolUi.Placeholder("Inherit")
    @DisplayName("Timed Companions")
    private CascadingTimedCompanionList timedCompanionList;

    @ToolUi.CollectionItemWeight(calculated = true)
    private double weight;

    @ToolUi.CollectionItemWeightColor
    private String weightColor;

    @ToolUi.CollectionItemWeightMarker
    private List<Double> markers;

    @Override
    public TimedContent getTimedContent() {
        return timedContent;
    }

    public void setTimedContent(TimedContent timedContent) {
        this.timedContent = timedContent;
    }

    public CascadingTimedCompanionList getTimedCompanionList() {
        updateTimedCompanionList();
        return timedCompanionList;
    }

    public void setTimedCompanionList(CascadingTimedCompanionList timedCompanionList) {
        this.timedCompanionList = timedCompanionList;
    }

    public List<TimedCompanion> getTimedCompanions() {
        return CascadingTimedCompanionList.getItemsMerged(this, getTimedCompanionList());
    }

    public String getTimedCompanionListNoteHtml(ToolPageContext page) {
        return new CascadingTimedCompanionListNoteRenderer(this, getTimedCompanionList())
            .render(this, getState().getField("timedCompanionList"), page);
    }

    void setWeight(double weight) {
        this.weight = weight;
    }

    void setWeightColor(String weightColor) {
        this.weightColor = weightColor;
    }

    void setMarkers(List<Double> markers) {
        this.markers = markers;
    }

    private void updateTimedCompanionList() {
        if (timedCompanionList != null) {
            timedCompanionList.updateItems(timedContent);
        }
    }

    /**
     * @return The duration of this playlist item (in milliseconds). If the duration is unknown, null is returned.
     */
    public Long getDuration() {
        return timedContent != null ? timedContent.getTimedContentDuration() : null;
    }

    @Override
    public String getLabel() {

        if (timedContent == null) {
            return "Untitled";
        }

        String durationLabel = DurationUtils.durationToLabel(DurationUtils.getTimedContentDuration(timedContent));

        StringBuilder label = new StringBuilder();

        label.append(timedContent.getState().getType().getLabel());
        label.append(": ");

        if (durationLabel != null) {
            label.append("(").append(durationLabel).append(") ");
        }

        label.append(timedContent.getState().getLabel());

        return label.toString();
    }

    @Override
    public TimedContent getTimedContentItemContent() {
        return getTimedContent();
    }

    @Override
    public Long getTimedContentItemDuration() {
        return getDuration();
    }

    @Override
    protected void beforeSave() {
        super.beforeSave();
        updateTimedCompanionList();
    }
}
