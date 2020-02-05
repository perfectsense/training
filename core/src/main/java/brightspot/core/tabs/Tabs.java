package brightspot.core.tabs;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.core.anchor.Anchorable;
import brightspot.core.anchor.Anchorage;
import brightspot.core.image.ImageOption;
import brightspot.core.lead.Lead;
import brightspot.core.module.ModuleType;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.util.StringUtils;

public class Tabs extends ModuleType implements
    Anchorage,
    Lead {

    private String title;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    private List<TabItem> tabs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TabItem> getTabs() {
        if (tabs == null) {
            return new ArrayList<>();
        }
        return tabs;
    }

    public void setTabs(List<TabItem> tabs) {
        this.tabs = tabs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public ImageOption getLeadImage() {
        return null;
    }

    @Override
    public Set<Anchorable> getAnchors() {

        // LinkedHashSet to maintain order of items
        Set<Anchorable> anchors = new LinkedHashSet<>();

        // adding the anchor(s) of the tabs
        getTabs().stream()
            .map(Anchorage::getAnchorsForObject)
            .flatMap(Set::stream)
            .forEach(anchors::add);

        return anchors;
    }

    @Override
    public String getLabel() {
        String title = getTitle();

        if (!StringUtils.isBlank(title)) {
            return title;
        }

        String tabItemsLabel = getTabs().stream()
            .map(Record::getLabel)
            .collect(Collectors.joining(", "));

        if (!StringUtils.isBlank(tabItemsLabel)) {
            return tabItemsLabel;
        }

        return super.getLabel();
    }
}
