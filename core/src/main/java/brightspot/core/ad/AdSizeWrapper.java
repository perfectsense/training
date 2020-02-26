package brightspot.core.ad;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * @deprecated Class no longer necessary.
 *
 * Wraps an AdSize for use as in a {@link AdsFrontEndSettingsModification}.
 */
@Deprecated
@Recordable.DisplayName("Ad Size")
@Recordable.LabelFields("adSize")
@Recordable.Embedded
public class AdSizeWrapper extends Record {

    @Required
    @Embedded
    private AdSize adSize;

    public AdSize getAdSize() {
        return adSize;
    }

    public void setAdSize(AdSize adSize) {
        this.adSize = adSize;
    }
}
