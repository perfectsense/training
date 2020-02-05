package brightspot.core.video;

import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class VideoOption extends Record {

    public abstract Video getVideoOptionVideo();

    public abstract Long getVideoOptionDuration();

    public static VideoOption createDefault() {
        return DefaultImplementationSupplier.createDefault(VideoOption.class, SharedVideoOption.class);
    }

}
