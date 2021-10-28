package training;

import brightspot.core.image.OneOffImageOption;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;

@JsonView
@ViewInterface
public class OneOffImagePageHeadingViewModel extends ExportViewModel<OneOffImageOption> implements ModulePageLeadView {

    @ViewKey("backgroundImage")
    public RefView getBackgroundImage() {
        return createView(RefView.class, model);
    }

    @Override
    public String getType() {
        return ExportUtils.getExportType(this);
    }
}
