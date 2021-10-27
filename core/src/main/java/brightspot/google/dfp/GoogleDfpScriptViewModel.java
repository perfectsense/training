package brightspot.google.dfp;

import brightspot.module.ad.AbstractAdViewModel;
import com.psddev.styleguide.dfp.GoogleDfpScriptView;

/**
 * Renders a {@link GoogleDfp} as a {@link GoogleDfpScriptView}.
 */
public class GoogleDfpScriptViewModel extends AbstractAdViewModel<GoogleDfp> implements GoogleDfpScriptView {

    @Override
    public CharSequence getTargets() {
        return null;
    }
}
