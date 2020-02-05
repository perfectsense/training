package brightspot.core.permalink;

import java.util.function.Function;

import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;

@Recordable.Embedded
public abstract class AbstractPermalinkRule extends Record {

    @DisplayName("Auto Increment?")
    @ToolUi.Placeholder("Inherit")
    @ToolUi.NoteHtml("<span data-dynamic-html='${content.getAutoIncrementNote()}'></span>")
    private SequentialDirectoryItemOption autoIncrementOption;

    public SequentialDirectoryItemOption getAutoIncrementOption() {
        return autoIncrementOption;
    }

    public void setAutoIncrementOption(SequentialDirectoryItemOption autoIncrementOption) {
        this.autoIncrementOption = autoIncrementOption;
    }

    public abstract String createPermalink(Site site, Object object);

    /**
     * Returns {@code true} or {@code false} for whether this permalink rule should auto increment the generated path
     * string, taking into account both the instance-specific setting (e.g., "Inherit" vs. "Disabled" vs. "Enabled") as
     * well as the global CMS setting.
     */
    boolean shouldAutoIncrement() {
        boolean globalSetting = SequentialDirectoryItemData.isGlobalAutoIncrementPermalinks();

        if (autoIncrementOption == null) {
            // "Inherit"
            return globalSetting;
        }

        return SequentialDirectoryItemOption.Enabled.equals(autoIncrementOption);
    }

    /**
     * Not for external use
     **/
    public String getAutoIncrementNote() {
        boolean isGloballyEnabled = SequentialDirectoryItemData.isGlobalAutoIncrementPermalinks();

        if (autoIncrementOption == null) {
            // "Inherit"
            return isGloballyEnabled ? "Enabled" : "Disabled";
        }
        return null;
    }

    /**
     * Creates a permalink path {@code String} given a {@link Site}, {@code Object}, and default permalink rule
     * implementation class.  This method first consults global CMS settings for a {@link PermalinkRuleOverride} for the
     * {@link ObjectType} for the given object.  If not found, falls back on the provided default {@code
     * AbstractPermalinkRule}, if applicable.
     *
     * @param object Always returns null if the object doesn't have a {@link State}
     */
    public static String create(Site site, Object object, Class<? extends AbstractPermalinkRule> defaultRuleClass) {
        State state = State.getInstance(object);

        if (state == null) {
            return null;
        }

        AbstractPermalinkRule rule = PermalinkRuleSettings.get(object);

        if (rule == null && defaultRuleClass != null) {
            ObjectType objectType = ObjectType.getInstance(defaultRuleClass);

            if (objectType != null) {
                rule = (AbstractPermalinkRule) objectType.createObject(null);
            }
        }

        if (rule == null) {
            return null;
        }

        return effectivelyCreate(state, rule.createPermalink(site, object), rule.shouldAutoIncrement());
    }

    /**
     * Creates a permalink path {@code String} given a {@link Site}, {@code Object}, and {@link Function} This method
     * first consults global CMS settings for a {@link PermalinkRuleOverride} for the {@link ObjectType} for the given
     * object.  If not found, calls the provided function for a fallback, if applicable.
     *
     * @param object Always returns null if the object doesn't have a {@link State}
     */
    public static String create(Site site, Object object, Function<Site, String> function) {
        State state = State.getInstance(object);

        if (state == null) {
            return null;
        }

        String pathString = null;
        boolean autoIncrement = false;
        AbstractPermalinkRule rule = PermalinkRuleSettings.get(object);

        if (rule != null) {
            pathString = rule.createPermalink(site, object);
            autoIncrement = rule.shouldAutoIncrement();
        } else if (function != null) {
            pathString = function.apply(site);
            // Consult global setting
            autoIncrement = SequentialDirectoryItemData.isGlobalAutoIncrementPermalinks();
        }

        return effectivelyCreate(state, pathString, autoIncrement);
    }

    // Encapsulates auto-increment append logic
    private static String effectivelyCreate(State objectState, String pathString, boolean autoIncrement) {
        if (pathString == null) {
            return null;
        }

        if (autoIncrement) {
            SequentialDirectoryItemData sequentialDirectoryItemData = objectState.as(SequentialDirectoryItemData.class);

            if (sequentialDirectoryItemData != null) {

                Integer index = objectState.as(SequentialDirectoryItemData.class).getDirectoryItemIndex();
                if (index != null) {
                    pathString = pathString + "-" + index;
                }
            }
        }

        return pathString;
    }

}
