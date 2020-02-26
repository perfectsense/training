package brightspot.comscore;

import brightspot.core.thirdpartyintegration.AbstractThirdPartyViewModel;
import com.psddev.styleguide.comscore.ComScoreScriptView;

public class ComScoreViewModel extends AbstractThirdPartyViewModel<ComScore> implements ComScoreScriptView {

    /**
     * @return The ComScore ClientId configured in {@link brightspot.core.site.FrontEndSettings#getIntegrations()}.
     */
    @Override
    public CharSequence getClientId() {
        return model.getClientId();
    }

    @Override
    public CharSequence getGenre() {
        return "";
    }

    @Override
    public CharSequence getClientSegment() {
        return "";
    }
}
