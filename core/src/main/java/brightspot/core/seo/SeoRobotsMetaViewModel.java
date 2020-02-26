package brightspot.core.seo;

import com.psddev.cms.db.Seo;
import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.Recordable;
import com.psddev.styleguide.core.page.SeoRobotsMetaView;

public class SeoRobotsMetaViewModel extends ViewModel<Recordable> implements SeoRobotsMetaView {

    @Override
    public CharSequence getRobotsString() {
        return model.as(Seo.ObjectModification.class).findRobotsString();
    }
}
