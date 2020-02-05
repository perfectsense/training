package brightspot.core.thirdpartyintegration;

import brightspot.core.site.FrontEndSettings;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.HttpParameter;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractThirdPartyViewModel<M> extends ViewModel<M> {

    @CurrentSite
    protected Site site;

    @HttpParameter
    protected String disable3rdParty;

    @Override
    protected boolean shouldCreate() {
        return StringUtils.isBlank(disable3rdParty)
            || !StringUtils.equals(
            disable3rdParty,
            FrontEndSettings.get(site, FrontEndSettings::getDisableThirdPartyParameterValue));
    }
}
