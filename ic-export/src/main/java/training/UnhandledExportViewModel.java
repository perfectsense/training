package training;

import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.dari.db.Recordable;

@JsonView
@ViewInterface
public class UnhandledExportViewModel extends ExportViewModel<Recordable> implements ExportEntryView {

    @Override
    protected boolean shouldCreate() {
        throw new IllegalArgumentException("Unhandled type for ExportEntryView: " + model.getClass().getName());
    }
}
