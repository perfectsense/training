package brightspot.core.action.actionbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.ObjectUtils;

public class ActionBarSettings extends Modification<SiteSettings> {

    @ToolUi.Cluster("Action Bar")
    @ToolUi.Tab("Front-End")
    @Embedded
    private ActionBar defaultActionBar;

    @ToolUi.Cluster("Action Bar")
    @ToolUi.Tab("Front-End")
    @ToolUi.Note("Where applicable, this will be appended as a tracking parameter to the URL for the action bar item.")
    private String trackingParameterName;

    @ToolUi.Cluster("Action Bar")
    @ToolUi.Tab("Front-End")
    @ToolUi.DisplayName("Type Specific Overrides")
    private List<ActionBarSetting> actionBarSettings;

    public List<ActionBarSetting> getActionBarSettings() {
        if (actionBarSettings == null) {
            actionBarSettings = new ArrayList<>();
        }
        return actionBarSettings;
    }

    public void setActionBarSettings(List<ActionBarSetting> actionBarSettings) {
        this.actionBarSettings = actionBarSettings;
    }

    @Override
    protected void beforeSave() {
        List<ObjectType> actionBarTypes = getActionBarSettings()
            .stream().flatMap(item -> item.getTypes().stream())
            .collect(Collectors.toList());

        if (!ObjectUtils.isBlank(actionBarTypes) && new HashSet<>(actionBarTypes).size() < actionBarTypes.size()) {
            getState().addError(
                getState().getField("shareBarSettings"),
                "Cannot add shareable type for more than one share bar setting.");
        }
    }

    public ActionBar getDefaultActionBar() {
        return defaultActionBar;
    }

    public void setDefaultActionBar(ActionBar defaultActionBar) {
        this.defaultActionBar = defaultActionBar;
    }

    public String getTrackingParameterName() {
        return trackingParameterName;
    }

    public void setTrackingParameterName(String trackingParameterName) {
        this.trackingParameterName = trackingParameterName;
    }
}
