package brightspot.event;

import com.psddev.dari.db.Modification;

public class LiveContentData extends Modification<LiveContent> {

    @Indexed
    private boolean isLive;

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

}
