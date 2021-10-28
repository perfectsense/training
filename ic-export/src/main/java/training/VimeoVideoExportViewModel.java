package training;

import java.util.Map;
import java.util.Optional;

import brightspot.vimeo.VimeoUrlVideoProvider;
import com.psddev.cms.db.ExternalContent;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;
import com.psddev.cms.view.ViewResponse;

@JsonView
@ViewInterface
public class VimeoVideoExportViewModel extends AbstractVideoExportViewModel {

    private VimeoUrlVideoProvider videoProvider;

    @Override
    protected void onCreate(ViewResponse response) {
        videoProvider = (VimeoUrlVideoProvider) model.getVideoProvider();
    }

    @ViewKey("vimeoIdentifier")
    public String getVimeoIdentifier() {
        return videoProvider.getVimeoIdentifier();
    }

    @ViewKey("externalContent")
    public Map<String, Object> getExternalContent() {
        return Optional.ofNullable(videoProvider.getExternalContent())
            .map(ExternalContent::getResponse)
            .filter(r -> !r.isEmpty())
            .orElse(null);
    }

    @ViewKey("calledExternalContent")
    public boolean getCalledExternalContent() {
        return Optional.ofNullable(videoProvider.getExternalContent())
            .map(ExternalContent::getResponse)
            .map(r -> !r.isEmpty())
            .orElse(false);
    }
}
