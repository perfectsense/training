package brightspot.image;

import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("None")
public class CascadingWebImageNone extends CascadingWebImage {

    @Override
    public WebImage get() {
        return null;
    }
}
