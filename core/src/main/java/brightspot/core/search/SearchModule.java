package brightspot.core.search;

import brightspot.core.module.ModuleType;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;

@Recordable.DisplayName("Search")
public class SearchModule extends ModuleType {

    @ToolUi.Placeholder(dynamicText = "${content.placeholderFallback}", editable = true)
    private String placeholder;

    public String getPlaceholder() {
        if (StringUtils.isBlank(placeholder)) {
            return getPlaceholderFallback();
        }
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    protected void beforeSave() {
        if (placeholder == null) {
            setPlaceholder(getPlaceholder());
        }
    }

    /**
     * Not for external use!
     */
    public String getPlaceholderFallback() {
        return "What are you looking for today?";
    }
}
