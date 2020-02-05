package brightspot.core.containermodule;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import brightspot.core.module.ModuleType;
import brightspot.core.tool.SmallRichTextToolbar;
import com.psddev.cms.db.ToolUi;

/**
 * A {@link ModuleType} used to separate other module types into columns.
 */
public class ContainerModule extends ModuleType {

    private String title;

    @ToolUi.RichText(toolbar = SmallRichTextToolbar.class)
    private String description;

    @Deprecated
    private ContainerColumnOption columns = ContainerColumnOption.createDefault();

    @Embedded
    @Required
    private List<ContainerColumnOption> rows;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Deprecated
    public ContainerColumnOption getColumns() {
        return columns;
    }

    @Deprecated
    public void setColumns(ContainerColumnOption columns) {
        this.columns = columns;
    }

    public List<ContainerColumnOption> getRows() {
        if (rows == null) {
            rows = new ArrayList<>();
        }

        relocateColumn(); // To relocate legacy columns.
        return rows;
    }

    public void setRows(List<ContainerColumnOption> rows) {
        this.rows = rows;
    }

    @Override
    public Set<UUID> getModuleTypeContentIds() {
        return getRows().stream()
                .map(ContainerColumnOption::getContainerContentIds)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    @Relocate
    public void relocateColumn() {

        if (this.rows == null) {
            this.rows = new ArrayList<>();
        }

        if (this.rows.isEmpty() && columns != null) {
            this.rows.add(columns);
            this.columns = null;
        }
    }
}
