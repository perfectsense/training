package brightspot.core.tabs;

import brightspot.core.tool.RichTextUtils;
import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.tab.TabsView;
import com.psddev.styleguide.core.tab.TabsViewTabsField;

public class TabsViewModel extends ViewModel<Tabs> implements TabsView {

    @Override
    public String getTitle() {
        return model.getTitle();
    }

    @Override
    public CharSequence getDescription() {
        return RichTextUtils.buildInlineHtml(model.getState().getDatabase(), model.getDescription(), this::createView);
    }

    @Override
    public Iterable<? extends TabsViewTabsField> getTabs() {
        return createViews(TabsViewTabsField.class, model.getTabs());
    }
}
