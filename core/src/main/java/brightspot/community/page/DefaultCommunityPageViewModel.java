package brightspot.community.page;

import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.Recordable;

public class DefaultCommunityPageViewModel extends ViewModel<Recordable> {

    public <T> Iterable<T> getComments(Class<T> viewClass) {
        return null;
    }

    public CharSequence getTimedContentTimeStamp() {
        return null;
    }
}
