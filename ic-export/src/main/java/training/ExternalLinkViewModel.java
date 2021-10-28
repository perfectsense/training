package training;

import brightspot.core.link.ExternalLink;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;

@JsonView
@ViewInterface
public class ExternalLinkViewModel extends LinkViewModel<ExternalLink> {

    @ViewKey("url")
    public String getUrl() {
        return model.getUrl();
    }
}
