package training;

import brightspot.core.link.InternalLink;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;

@JsonView
@ViewInterface
public class InternalLinkViewModel extends LinkViewModel<InternalLink> {

    @ViewKey("anchor")
    public String getAnchor() {
        return model.getAnchor();
    }

    @ViewKey("item")
    public RefView getItem() {
        return createView(RefView.class, model.getItem());
    }
}
