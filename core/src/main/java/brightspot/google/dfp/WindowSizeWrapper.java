package brightspot.google.dfp;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * @deprecated Class no longer necessary.
 *
 * Provides a mechanism to define {@link WindowSize} configurations at the site level.
 */
@Deprecated
@Recordable.DisplayName("Window Size")
@Recordable.LabelFields("windowSize")
@Recordable.Embedded
public class WindowSizeWrapper extends Record {

    @Required
    @Embedded
    private WindowSize windowSize;

    /**
     * @return Unwraps {{@link #windowSize}}.
     */
    public WindowSize getWindowSize() {
        return windowSize;
    }

    /**
     * @param windowSize Window size.
     */
    public void setWindowSize(WindowSize windowSize) {
        this.windowSize = windowSize;
    }
}
