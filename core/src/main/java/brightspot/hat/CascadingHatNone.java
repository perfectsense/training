package brightspot.hat;

import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("None")
public class CascadingHatNone extends CascadingHat {

    @Override
    public Hat get() {
        return null;
    }
}
