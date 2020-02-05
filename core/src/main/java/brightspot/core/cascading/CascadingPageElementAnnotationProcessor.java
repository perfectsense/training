package brightspot.core.cascading;

import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.ObjectUtils;

public class CascadingPageElementAnnotationProcessor implements ObjectField.AnnotationProcessor<CascadingPageElement> {

    private static final String HEADING_SUFFIX = " Overrides";
    private static final String DEFAULTS_TAB = "Page Defaults";
    private static final String OVERRIDES_TAB = "Overrides";

    @Override
    public void process(ObjectType objectType, ObjectField objectField, CascadingPageElement cascadingPageElement) {

        if (objectType != null && (Site.class.equals(objectType.getObjectClass())
            || CmsTool.class.equals(objectType.getObjectClass()))) {
            objectField.as(ToolUi.class).setTab(DEFAULTS_TAB);

        } else {
            objectField.as(ToolUi.class).setTab(OVERRIDES_TAB);
            String heading = objectField.as(ToolUi.class).getHeading();
            if (!ObjectUtils.isBlank(heading) && !heading.endsWith(HEADING_SUFFIX)) {
                objectField.as(ToolUi.class).setHeading(heading + HEADING_SUFFIX);
            }
        }

        objectField.as(ToolUi.class)
            .setNoteHtml("<span data-dynamic-html=\"${content.as['" + CascadingPageElementsModification.INTERNAL_NAME
                + "'].createNoteHtml('" + objectField.getInternalName() + "')}\"></span>");

        objectField.as(ToolUi.class).setPlaceholderDynamicText("${modification.getInheritPlaceholder()}");
    }
}
