package brightspot.core.requestextras.headelement;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class StyleElementBody extends Record {

    private transient String media;

    public void setMedia(String media) {
        this.media = media;
    }

    public String getMedia() {
        return media;
    }
}
