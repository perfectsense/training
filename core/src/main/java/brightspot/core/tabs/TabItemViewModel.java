package brightspot.core.tabs;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.tab.TabItemView;
import com.psddev.styleguide.core.tab.TabItemViewContentField;

public class TabItemViewModel extends ViewModel<TabItem> implements TabItemView {

    @Override
    public CharSequence getLabel() {
        return model.getLabel();
    }

    @Override
    public CharSequence getTitle() {
        return model.getTitle();
    }

    @Override
    public CharSequence getId() {
        return model.getAnchorableAnchor();
    }

    @Override
    public Iterable<? extends TabItemViewContentField> getContent() {
        return createViews(TabItemViewContentField.class, model.getTabContent());
    }
}
