package brightspot.ap;

import com.psddev.ap.AssociatedPressSettings;
import com.psddev.cms.tool.Search;
import com.psddev.cms.tool.search.ExternalItemSearchResultView;

public class AssociatedPressImageSearchResultView extends ExternalItemSearchResultView<AssociatedPressImageToWebImageConverter> {

    @Override
    public boolean isSupported(Search search) {
        return AssociatedPressSettings.isEnabled() && super.isSupported(search);
    }
}
