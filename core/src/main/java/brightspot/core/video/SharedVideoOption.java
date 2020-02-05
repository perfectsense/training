package brightspot.core.video;

import java.util.Optional;

import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Shared")
public class SharedVideoOption extends VideoOption {

    @Required
    private Video video;

    @Override
    public Video getVideoOptionVideo() {
        return Optional.ofNullable(getVideo())
            .orElse(null);
    }

    @Override
    public Long getVideoOptionDuration() {
        return Optional.ofNullable(getVideo())
            .map(Video::getDuration)
            .orElse(null);
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }
}
