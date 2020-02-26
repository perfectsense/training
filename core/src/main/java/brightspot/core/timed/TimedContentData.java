package brightspot.core.timed;

import java.util.ArrayList;
import java.util.List;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Modification;

/**
 * Modification of {@linkplain TimedContent} that allows for attaching companion content.
 */
public class TimedContentData extends Modification<TimedContent> {

    @ToolUi.NoteHtml("<span data-dynamic-html='${modification.getTimedCompanionsNoteHtml(toolPageContext)}'></span>")
    @ToolUi.Tab("Companion Content")
    @ToolUi.Unlabeled
    private List<TimedCompanion> timedCompanions;

    public List<TimedCompanion> getTimedCompanions() {
        updateTimedCompanions();
        if (timedCompanions == null) {
            timedCompanions = new ArrayList<>();
        }
        return timedCompanions;
    }

    public void setTimedCompanions(List<TimedCompanion> timedCompanions) {
        this.timedCompanions = timedCompanions;
    }

    public String getTimedCompanionsNoteHtml(ToolPageContext toolPageContext) {
        return new TimedContentPlayerNoteRenderer().render(
            getOriginalObject(),
            getState().getField("timedCompanions"),
            toolPageContext,
            true);
    }

    @Override
    public void beforeSave() {
        updateTimedCompanions();
    }

    private void updateTimedCompanions() {
        if (timedCompanions != null) {
            timedCompanions.forEach(tc -> tc.setTimedContent(getOriginalObject()));
        }
        TimedCompanion.sortList(timedCompanions);
    }
}
