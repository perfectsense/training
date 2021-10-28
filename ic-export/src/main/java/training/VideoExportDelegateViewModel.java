package training;

import brightspot.core.video.Video;
import brightspot.core.video.VideoProvider;
import brightspot.google.youtube.YouTubeUrlVideoProvider;
import brightspot.vimeo.VimeoUrlVideoProvider;
import com.psddev.cms.view.DelegateView;
import com.psddev.cms.view.ViewModel;

public class VideoExportDelegateViewModel extends ViewModel<Video> implements
    DelegateView<ExportViewModel<Video>>,
    ExportEntryView {

    @Override
    protected boolean shouldCreate() {
        return model.getVideoProvider() != null;
    }

    @Override
    public ExportViewModel<Video> getDelegate() {
        VideoProvider videoProvider = model.getVideoProvider();

        if (videoProvider instanceof VimeoUrlVideoProvider) {
            return createView(VimeoVideoExportViewModel.class, model);

        } else if (videoProvider instanceof YouTubeUrlVideoProvider) {
            return createView(YouTubeVideoExportViewModel.class, model);

        } else {
            throw new IllegalArgumentException("Unhandled VideoProvider " + videoProvider.getClass().getName());
        }
    }
}
