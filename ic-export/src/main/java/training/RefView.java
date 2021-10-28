package training;

import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;

@JsonView
@ViewInterface
public interface RefView {

    @ViewKey("_ref")
    String getRef();

    @ViewKey("_type")
    String getType();
}
