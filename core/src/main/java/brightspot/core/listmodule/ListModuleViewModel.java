package brightspot.core.listmodule;

import java.util.Optional;

import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.styleguide.core.list.ListView;
import com.psddev.styleguide.core.list.ListViewItemsField;
import com.psddev.styleguide.core.list.ListViewPaginationField;

public class ListModuleViewModel extends AbstractListModuleViewModel<ListModule> implements ListView {

    @CurrentSite
    Site currentSite;

    @Override
    public CharSequence getDescription() {
        return RichTextUtils.buildInlineHtml(model.getState().getDatabase(), model.getDescription(), this::createView);
    }

    @Override
    public CharSequence getCtaUrl() {
        return Optional.ofNullable(model.getCallToAction())
            .map(l -> l.getLinkUrl(currentSite))
            .orElse(null);
    }

    @Override
    public CharSequence getCtaTarget() {
        return Optional.ofNullable(model.getCallToAction())
            .map(l -> l.getTarget())
            .map(t -> t.getValue())
            .orElse(null);
    }

    @Override
    public Iterable<? extends ListViewPaginationField> getPagination() {
        return getPaginationLinkViews();
    }

    @Override
    public Iterable<? extends ListViewItemsField> getItems() {
        return getItems(ListViewItemsField.class);
    }
}
