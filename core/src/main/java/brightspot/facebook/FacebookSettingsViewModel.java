package brightspot.facebook;

import brightspot.core.thirdpartyintegration.AbstractThirdPartyViewModel;
import com.psddev.styleguide.facebook.FacebookJavaScriptSdkView;

public class FacebookSettingsViewModel extends AbstractThirdPartyViewModel<FacebookSettings>
    implements FacebookJavaScriptSdkView {

    @Override
    public CharSequence getFacebookAppId() {
        // Plain text
        return model.getAppId();
    }
}
