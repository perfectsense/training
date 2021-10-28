package training;

import brightspot.core.image.OneOffImageOption;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;

@JsonView
@ViewInterface
public class OneOffImageLeadViewModel extends ExportViewModel<OneOffImageOption> implements ArticleLeadView {

    @ViewKey("image")
    public RefView getImage() {
        return createView(RefView.class, model);
    }

    @Override
    public String getType() {
        return ExportUtils.getExportType(this);
    }
}
