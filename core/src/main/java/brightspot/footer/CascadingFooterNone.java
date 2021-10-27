package brightspot.footer;

import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("None")
public class CascadingFooterNone extends CascadingFooter {

    @Override
    public Footer get() {
        return null;
    }
}
