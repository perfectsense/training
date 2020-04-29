package bex.training.alphasort;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("alphaSortOverride.")
public class AlphaSortOverrideData extends Modification<AlphaSortOverride> {

    @DisplayName("Sort Value")
    @ToolUi.Cluster("Search")
    @ToolUi.Placeholder(dynamicText = "${content.getAlphaSortValueFallback()}")
    private String alphaSortValue;

    public String getAlphaSortValue() {
        return alphaSortValue;
    }

    public void setAlphaSortValue(String alphaSortValue) {
        this.alphaSortValue = alphaSortValue;
    }
}
