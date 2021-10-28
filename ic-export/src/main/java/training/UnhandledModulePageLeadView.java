package training;

import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.dari.db.Recordable;

@JsonView
@ViewInterface
public class UnhandledModulePageLeadView extends ExportViewModel<Recordable> implements ModulePageLeadView {

    @Override
    protected boolean shouldCreate() {
        throw new IllegalArgumentException("Unhandled type for ModulePageLeadView: " + model.getClass().getName());
    }
}
