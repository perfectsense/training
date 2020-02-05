package brightspot.core.footer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.psddev.cms.db.Localization;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
@Recordable.DisplayName("Column")
public class FooterColumn extends Record {

    @Required
    @TypesExclude(FooterColumns.class)
    private List<FooterModuleType> items;

    public List<FooterModuleType> getItems() {
        if (items == null) {
            items = new ArrayList<>();
        }
        return items;
    }

    public void setItems(List<FooterModuleType> items) {
        this.items = items;
    }

    @Override
    public String getLabel() {
        return getItems().stream()
            .map(item -> {
                if (item instanceof FooterShared) {
                    return Optional.ofNullable(
                        ((FooterShared) item).getModule())
                        .map(module -> module.getState())
                        .map(state -> state.getType())
                        .map(type -> Localization.currentUserText(type, "displayName"))
                        .orElse(null);
                } else {
                    return Optional.ofNullable(item.getState())
                        .map(state -> state.getType())
                        .map(type -> Localization.currentUserText(type, "displayName"))
                        .orElse(null);
                }
            })
            .collect(Collectors.joining(", "));
    }
}
