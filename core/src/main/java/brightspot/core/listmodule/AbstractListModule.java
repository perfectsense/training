package brightspot.core.listmodule;

import java.util.Optional;

import brightspot.core.module.ModuleType;
import com.psddev.dari.util.StringUtils;

public abstract class AbstractListModule extends ModuleType {

    public abstract ItemStream getItemStream();

    public abstract String getTitle();

    public String getTitleFallback() {
        return Optional.ofNullable(getItemStream())
            .map(ItemStream::getTitlePlaceholder)
            .orElse(null);
    }

    @Override
    public String getLabel() {
        String title = getTitle();

        if (!StringUtils.isBlank(title)) {
            return title;
        }

        ItemStream items = getItemStream();

        String itemStreamLabel = items != null
            ? items.getLabel()
            : null;

        if (!StringUtils.isBlank(itemStreamLabel)) {
            return itemStreamLabel;
        }

        return super.getLabel();
    }
}
