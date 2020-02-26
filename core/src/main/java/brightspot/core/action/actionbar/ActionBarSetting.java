package brightspot.core.action.actionbar;

import java.util.Set;
import java.util.stream.Collectors;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;

@Recordable.Embedded
@Recordable.DisplayName("Type Specific Action Bar")
public class ActionBarSetting extends Record {

    @Required
    @Embedded
    @ToolUi.Unlabeled
    private ActionBar actionBar;

    @CollectionMinimum(1)
    @Where("groups = brightspot.core.promo.Promotable and isAbstract = false and isEmbedded = false")
    @ToolUi.DropDown
    @ToolUi.DisplayFirst
    private Set<ObjectType> types;

    public ActionBar getActionBar() {
        return actionBar;
    }

    public void setActionBar(ActionBar actionBar) {
        this.actionBar = actionBar;
    }

    public Set<ObjectType> getTypes() {
        return types;
    }

    public void setTypes(Set<ObjectType> types) {
        this.types = types;
    }

    @Override
    public String getLabel() {
        if (!ObjectUtils.isBlank(types)) {
            return StringUtils.join(types.stream().map(ObjectType::getDisplayName).collect(Collectors.toList()), ", ");
        }
        return null;
    }
}
