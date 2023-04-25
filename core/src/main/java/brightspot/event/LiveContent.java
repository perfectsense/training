package brightspot.event;

import com.psddev.dari.db.Recordable;

public interface LiveContent extends Recordable {

    default LiveContentData asLiveContentData() {
        return as(LiveContentData.class);
    }
}
