package training;

import brightspot.core.video.Video;

public class VideoRefViewModel extends RefViewModel<Video> {

    @Override
    public String getType() {
        return ExportUtils.getExportType(model.getVideoProvider());
    }
}
