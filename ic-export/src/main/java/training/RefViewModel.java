package training;

import com.psddev.cms.view.ViewModel;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.web.WebRequest;

public class RefViewModel<T extends Recordable> extends ViewModel<T> implements RefView {

    public String getRef() {
        WebRequest.getCurrent().as(ExportRefsWebExtension.class).addRef(model);
        return model.getState().getId().toString();
    }

    public String getType() {
        return ExportUtils.getExportType(model);
    }
}
