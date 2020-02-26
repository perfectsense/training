package brightspot.core.cache;

import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;

public class CacheControlSettings extends Modification<SiteSettings> {

    @ToolUi.Tab("Front-End")
    @ToolUi.Cluster("Advanced")
    private Boolean enableCacheControl;

    public boolean isEnableCacheControl() {
        return Boolean.TRUE.equals(enableCacheControl);
    }

    public void setEnableCacheControl(Boolean enableCacheControl) {
        this.enableCacheControl = enableCacheControl;
    }
}
