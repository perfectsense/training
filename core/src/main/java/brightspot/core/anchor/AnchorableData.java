package brightspot.core.anchor;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.FieldInternalNamePrefix("anchorable.")
public class AnchorableData extends Modification<AnchorableWithOverrides> {

    private static final String ANCHORABLE_TAB = "Overrides";

    @ToolUi.Cluster("Anchor Overrides (Internal)")
    @ToolUi.Tab(ANCHORABLE_TAB)
    @ToolUi.Placeholder(dynamicText = "${content.getAnchorableAnchorFallback()}", editable = true)
    private String anchor;

    /**
     * Return the anchor if it's set, otherwise return the anchorable's fallback
     */
    @ToolUi.Hidden
    @Ignored(false)
    public String getAnchor() {

        return ObjectUtils.firstNonBlank(anchor, getOriginalObject().getAnchorableAnchorFallback());
    }

    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }
}
