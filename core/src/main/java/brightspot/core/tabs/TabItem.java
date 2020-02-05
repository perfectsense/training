package brightspot.core.tabs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import brightspot.core.anchor.AnchorableWithOverrides;
import brightspot.core.module.ModuleType;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StringUtils;

@Recordable.Embedded
public class TabItem extends Record implements
    AnchorableWithOverrides {

    @Required
    private String label;

    @ToolUi.Placeholder(dynamicText = "${content.getTitleFallback()}")
    private String title;

    private List<ModuleType> tabContent;

    @Override
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTitle() {
        return ObjectUtils.firstNonNull(title, getTitleFallback());
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ModuleType> getTabContent() {
        if (tabContent == null) {
            return new ArrayList<>();
        }
        return tabContent;
    }

    public void setTabContent(List<ModuleType> tabContent) {
        this.tabContent = tabContent;
    }

    public String getTitleFallback() {
        return getLabel();
    }

    @Override
    public String getAnchorableAnchorFallback() {
        return Optional.ofNullable(getTitle())
            .map(StringUtils::toNormalized)
            .orElse(null);
    }
}
