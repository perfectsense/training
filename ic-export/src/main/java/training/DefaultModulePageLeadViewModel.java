package training;

import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;
import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.UuidUtils;

@JsonView
@ViewInterface
public class DefaultModulePageLeadViewModel extends ViewModel<Recordable> implements ModulePageLeadView {

    @ViewKey("_id")
    public String getId() {
        return UuidUtils.createSequentialUuid().toString();
    }

    @ViewKey("_type")
    public String getType() {
        return ExportUtils.getExportType(this);
    }
}
