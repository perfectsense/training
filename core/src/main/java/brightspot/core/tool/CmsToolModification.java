package brightspot.core.tool;

import java.util.HashSet;
import java.util.Set;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("express.cmsToolModification.")
@Recordable.TypePostProcessorClasses(AutoPluginValuesFinder.class)
public class CmsToolModification extends Modification<CmsTool> {

    public static final String DISABLED_PLUGINS_FIELD_NAME = "express.cmsToolModification.disabledCustomPlugins";

    @ToolUi.Tab("Debug")
    private String defaultTaskHost;

    @ToolUi.Tab("Debug")
    private Set<String> disabledCustomPlugins;

    @ToolUi.Tab("Debug")
    @ToolUi.Note("Auto-increments permalinks to prevent duplicate errors from being thrown.")
    private boolean autoIncrementPermalinks;

    public String getDefaultTaskHost() {
        return defaultTaskHost;
    }

    public void setDefaultTaskHost(String defaultTaskHost) {
        this.defaultTaskHost = defaultTaskHost;
    }

    public Set<String> getDisabledCustomPlugins() {
        if (disabledCustomPlugins == null) {
            disabledCustomPlugins = new HashSet<>();
        }
        return disabledCustomPlugins;
    }

    public void setDisabledCustomPlugins(Set<String> disabledPlugins) {
        this.disabledCustomPlugins = disabledPlugins;
    }

    public boolean isAutoIncrementPermalinks() {
        return autoIncrementPermalinks;
    }

    public void setAutoIncrementPermalinks(boolean autoIncrementPermalinks) {
        this.autoIncrementPermalinks = autoIncrementPermalinks;
    }
}
