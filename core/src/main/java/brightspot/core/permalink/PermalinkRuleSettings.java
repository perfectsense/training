package brightspot.core.permalink;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;

public class PermalinkRuleSettings extends Modification<CmsTool> {

    @ToolUi.Cluster("Permalinks")
    @ToolUi.Tab("Front-End")
    private List<PermalinkRuleOverride> permalinkRuleOverrides;

    public List<PermalinkRuleOverride> getPermalinkRuleOverrides() {
        if (permalinkRuleOverrides == null) {
            permalinkRuleOverrides = new ArrayList<>();
        }
        return permalinkRuleOverrides;
    }

    public void setPermalinkRuleOverrides(List<PermalinkRuleOverride> permalinkRuleOverrides) {
        this.permalinkRuleOverrides = permalinkRuleOverrides;
    }

    /**
     * Consults global settings for an {@link AbstractPermalinkRule} implementation matching the given {@code Object}.
     *
     * @param object if {@code null} or doesn't contain a {@link State}, this method always returns {@code null}.
     */
    public static AbstractPermalinkRule get(Object object) {

        ObjectType type = Optional.ofNullable(object)
            .map(State::getInstance)
            .map(State::getType)
            .orElse(null);

        if (type == null) {
            return null;
        }

        PermalinkRuleSettings settings = Query.from(CmsTool.class).first().as(PermalinkRuleSettings.class);

        for (PermalinkRuleOverride mapping : settings.getPermalinkRuleOverrides()) {
            if (type.equals(mapping.getType())) {
                return mapping.getRule();
            }
        }

        return null;
    }
}
