package brightspot.core.permalink;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * Simple embedded record class to encapsulate a mapping of a {@link ObjectType} to an {@link AbstractPermalinkRule}
 * implementation instance.
 */
@Recordable.Embedded
@Recordable.DisplayName("Override")
public class PermalinkRuleOverride extends Record {

    @Where("groups = brightspot.core.page.Page and isAbstract = false")
    @Required
    private ObjectType type;

    @ToolUi.DropDown
    @Required
    private AbstractPermalinkRule rule;

    public ObjectType getType() {
        return type;
    }

    public void setType(ObjectType type) {
        this.type = type;
    }

    public AbstractPermalinkRule getRule() {
        return rule;
    }

    public void setRule(AbstractPermalinkRule rule) {
        this.rule = rule;
    }

    @Override
    public String getLabel() {
        String label = null;

        if (type != null && rule != null) {
            label = type.getDisplayName() + " : " + rule.getLabel();
        }
        return label;
    }
}
