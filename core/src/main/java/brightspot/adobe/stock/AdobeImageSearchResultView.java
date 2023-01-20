package brightspot.adobe.stock;

import com.psddev.adobe.stock.AdobeStockSettings;
import com.psddev.cms.tool.Search;
import com.psddev.cms.tool.search.ExternalItemSearchResultView;

public class AdobeImageSearchResultView extends ExternalItemSearchResultView<AdobeImageToWebImageConverter> {

    @Override
    public boolean isSupported(Search search) {
        AdobeStockSettings adobeStockSettings = new AdobeStockSettings();
        return adobeStockSettings.getApiClient() != null && super.isSupported(search);
    }
}
