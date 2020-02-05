package brightspot.core.footer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import brightspot.core.module.BaseModuleType;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class FooterColumns extends Record implements FooterModuleType, BaseModuleType, FooterModuleTypeOnly {

    @Required
    private List<FooterColumn> columns;

    public List<FooterColumn> getColumns() {
        if (columns == null) {
            columns = new ArrayList<>();
        }
        return columns;
    }

    public void setColumns(List<FooterColumn> columns) {
        this.columns = columns;
    }

    @Override
    public String getLabel() {
        return getColumns().stream()
            .map(FooterColumn::getLabel)
            .collect(Collectors.joining(", "));
    }
}
