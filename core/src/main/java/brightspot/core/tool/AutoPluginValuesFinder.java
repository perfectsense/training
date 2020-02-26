package brightspot.core.tool;

import java.util.LinkedHashSet;
import java.util.Set;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.ClassFinder;
import com.psddev.dari.util.StringUtils;

public class AutoPluginValuesFinder implements ObjectType.PostProcessor {

    @Override
    public void process(ObjectType type) {

        ObjectType cmsToolType = ObjectType.getInstance(CmsTool.class);
        ObjectField disabledPluginsField = cmsToolType.getField(CmsToolModification.DISABLED_PLUGINS_FIELD_NAME);

        if (disabledPluginsField != null) {
            Set<ObjectField.Value> values = new LinkedHashSet<>();
            for (Class<? extends AutoPlugin> cls : ClassFinder.findConcreteClasses(AutoPlugin.class)) {
                if (AutoPlugin.class.isAssignableFrom(cls)) {
                    ObjectField.Value value = new ObjectField.Value();
                    value.setValue(cls.getName());
                    value.setLabel(StringUtils.toLabel(cls.getSimpleName()));
                    values.add(value);
                }
            }

            disabledPluginsField.setValues(values);

            if (values.isEmpty()) {
                disabledPluginsField.as(ToolUi.class).setHidden(true);
            }
        }
    }
}
