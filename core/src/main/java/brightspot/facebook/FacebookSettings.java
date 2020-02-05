package brightspot.facebook;

import brightspot.core.page.HeadScripts;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class FacebookSettings extends Record implements HeadScripts {

    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
