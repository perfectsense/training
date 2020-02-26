package brightspot.core.search;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.search.SearchControlView;

public class FilterItemSearchControlViewModel extends ViewModel<FilterItem> implements SearchControlView {

    @Override
    public CharSequence getLabel() {
        // Plain text
        return model.getLabel();
    }

    @Override
    public Boolean getSelected() {
        return model.isSelected();
    }

    @Override
    public CharSequence getUrl() {
        // Plain text
        return model.getUrl();
    }

    @Override
    public Number getCount() {
        return model.getCount();
    }

    @Override
    public CharSequence getParameterValue() {
        return model.getValue();
    }
}
