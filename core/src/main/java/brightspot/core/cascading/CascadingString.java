package brightspot.core.cascading;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
@Recordable.DisplayName("Replace")
public final class CascadingString extends Record implements Cascading<String> {

    @ToolUi.Unlabeled
    @ToolUi.Placeholder("Blank")
    private String value;

    @Override
    public String get() {
        return value;
    }
}
