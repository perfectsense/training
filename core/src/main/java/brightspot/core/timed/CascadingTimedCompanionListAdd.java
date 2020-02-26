package brightspot.core.timed;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.cascading.listmerge.AfterListMergeStrategy;
import brightspot.core.cascading.listmerge.ListMergeStrategy;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Recordable;

/**
 * A timed companion cascading list that add additional items to the inherited items.
 */
@Recordable.DisplayName("Add")
public class CascadingTimedCompanionListAdd extends CascadingTimedCompanionList {

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
        return new AfterListMergeStrategy<>();
    }

    public String getTimedCompanionsNoteHtml(ToolPageContext toolPageContext) {
        return new TimedContentPlayerNoteRenderer().render(this, getState().getField("items"), toolPageContext, true);
    }
}
