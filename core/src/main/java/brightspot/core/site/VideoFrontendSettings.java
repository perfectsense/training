package brightspot.core.site;

import java.util.HashSet;
import java.util.Set;

import brightspot.core.video.Option;
import brightspot.core.video.Preload;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable.Embedded;

@Embedded
public class VideoFrontendSettings extends Record {

    private Set<Option> options;

    private Preload preload;

    private Integer width;

    private Integer height;

    private Boolean controls;

    private Boolean loop;

    private String liveVideoNoContentMessage;

    // getters & setters

    public Set<Option> getOptions() {
        if (options == null) {
            return new HashSet<>();
        }
        return options;
    }

    public void setOptions(Set<Option> option) {
        this.options = options;
    }

    public Preload getPreload() {
        return preload;
    }

    public void setPreload(Preload preload) {
        this.preload = preload;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public boolean getControls() {
        return Boolean.TRUE.equals(controls);
    }

    public void setControls(Boolean controls) {
        this.controls = controls;
    }

    public Boolean getLoop() {
        return Boolean.TRUE.equals(loop);
    }

    public void setLoop(Boolean loop) {
        this.loop = loop;
    }

    public String getLiveVideoNoContentMessage() {
        return liveVideoNoContentMessage;
    }

    public void setLiveVideoNoContentMessage(String liveVideoNoContentMessage) {
        this.liveVideoNoContentMessage = liveVideoNoContentMessage;
    }
}
