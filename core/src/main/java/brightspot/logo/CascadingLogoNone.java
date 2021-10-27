package brightspot.logo;

import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("None")
public class CascadingLogoNone extends CascadingLogo {

    @Override
    public Logo get() {
        return null;
    }
}
