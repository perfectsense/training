package brightspot.core.module;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import brightspot.core.footer.FooterModuleType;
import brightspot.core.footer.FooterModuleTypeOnly;
import brightspot.core.footer.SharedFooterType;
import brightspot.core.image.ImageOption;
import brightspot.core.lead.LeadItem;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Localization;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ContentEditWidgetDisplay;
import com.psddev.cms.tool.content.UrlsWidget;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.ObjectUtils;

public class Module extends Content implements
    ContentEditWidgetDisplay,
    LeadItem,
    ModelWrapper, SharedFooterType {

    private static final String BASE_MODULE_TYPE_INTERNAL_NAME = "brightspot.core.module.BaseModuleType";
    private static final List<String> HIDDEN_WIDGETS = Arrays.asList(
        UrlsWidget.class.getName());

    @Required
    private String name;

    @Required
    @TypesExclude(Shared.class)
    private BaseModuleType type = ModuleType.createDefault();

    @DisplayName("Module Type")
    @Indexed
    @ToolUi.Hidden
    @ToolUi.Filterable
    @ToolUi.DropDown
    @Where("groups = " + BASE_MODULE_TYPE_INTERNAL_NAME
        + " && internalName != " + BASE_MODULE_TYPE_INTERNAL_NAME
        + " && isAbstract = false")
    private ObjectType moduleTypeObjectType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseModuleType getType() {
        return type;
    }

    public void setType(BaseModuleType type) {
        this.type = type;
    }

    @Override
    public ImageOption getLeadItemImage() {
        return Optional.ofNullable(getType())
            .filter(type -> type instanceof ModuleType)
            .map(type -> (ModuleType) type)
            .map(ModuleType::getModuleTypeImage)
            .orElse(null);
    }

    @Override
    public String getLabel() {
        StringBuilder label = new StringBuilder();
        BaseModuleType moduleType = getType();

        if (moduleType != null) {
            ObjectType type = moduleType.getState().getType();

            if (type != null) {
                label.append(Localization.currentUserText(type, "displayName"));
                label.append(": ");
            }
        }

        label.append(ObjectUtils.firstNonBlank(getName(), "Untitled"));

        return label.toString();
    }

    @Override
    public void beforeSave() {

        updateModuleTypeObjectType();
    }

    @Override
    public boolean shouldDisplayContentEditWidget(String widgetName) {
        return !HIDDEN_WIDGETS.contains(widgetName);
    }

    @Override
    public Object unwrap() {
        return getType();
    }

    private void updateModuleTypeObjectType() {
        if (type != null) {
            moduleTypeObjectType = type.getState().getType();
        }
    }

    @Override
    public boolean isFooterModuleType() {
        return getType() instanceof FooterModuleType;
    }

    @Override
    public boolean isFooterModuleTypeOnly() {
        return getType() instanceof FooterModuleTypeOnly;
    }
}
