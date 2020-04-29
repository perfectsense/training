package bex.training.alphasort;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.UnresolvedState;

@Recordable.FieldInternalNamePrefix(AlphaSortableData.FIELD_PREFIX)
public class AlphaSortableData extends Modification<AlphaSortable> {

    static final String FIELD_PREFIX = "alphaSortable.";
    public static final String ALPHA_SORT_VALUE_FIELD = FIELD_PREFIX + "getAlphaSortValue";

    @Indexed
    @ToolUi.Hidden
    public String getAlphaSortValue() {
        return UnresolvedState.resolveAndGet(getOriginalObject(), AlphaSortable::getAlphaSortValue);
    }
}
