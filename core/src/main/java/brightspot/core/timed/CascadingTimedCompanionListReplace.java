package brightspot.core.timed;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.cascading.listmerge.ListMergeStrategy;
import brightspot.core.cascading.listmerge.ReplaceListMergeStrategy;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Recordable;

/**
 * A timed companion cascading list that replaces any inherited items with its own.
 */
@Recordable.DisplayName("Replace")
public class CascadingTimedCompanionListReplace extends CascadingTimedCompanionList {

    @ToolUi.NoteHtml("<span data-dynamic-html='${content.getTimedCompanionsNoteHtml(toolPageContext)}'></span>")
    @ToolUi.Unlabeled
    private List<TimedCompanion> items;

    @Override
    public List<TimedCompanion> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    @Override
    protected ListMergeStrategy<TimedCompanion> getMergeStrategy() {
        return new ReplaceListMergeStrategy<>();
    }

    public String getTimedCompanionsNoteHtml(ToolPageContext toolPageContext) {
        return new TimedContentPlayerNoteRenderer().render(this, getState().getField("items"), toolPageContext, true);
    }
}
