package brightspot.core.search;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;

@Recordable.Embedded
public abstract class Sort extends Record {

    @ToolUi.Placeholder(dynamicText = "${content.defaultName}", editable = true)
    private String name;

    @ToolUi.Hidden
    private String parameterValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    public abstract String getDefaultName();

    public abstract void updateQuery(SiteSearchViewModel search, Query<?> query);

    @Override
    public String getLabel() {
        String name = getName();
        return !StringUtils.isBlank(name) ? name : getDefaultName();
    }
}
