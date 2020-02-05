package brightspot.core.page.opengraph.profile;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.facebook.OpenGraphProfileMetaView;

public class OpenGraphProfileMetaViewModel extends ViewModel<OpenGraphProfile> implements OpenGraphProfileMetaView {

    @Override
    public CharSequence getLastName() {
        return model.getOpenGraphLastName();
    }

    @Override
    public CharSequence getFirstName() {
        return model.getOpenGraphFirstName();
    }

    @Override
    public CharSequence getGender() {
        return model.getOpenGraphGender();
    }

    @Override
    public CharSequence getUsername() {
        return model.getOpenGraphUsername();
    }
}
