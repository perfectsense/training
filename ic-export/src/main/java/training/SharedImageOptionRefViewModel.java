package training;

import brightspot.core.image.SharedImageOption;
import com.psddev.dari.web.WebRequest;

public class SharedImageOptionRefViewModel extends RefViewModel<SharedImageOption> {

    @Override
    protected boolean shouldCreate() {
        return model.getImage() != null;
    }

    public String getRef() {
        WebRequest.getCurrent().as(ExportRefsWebExtension.class).addRef(model.getImage());
        return model.getImage().getId().toString();
    }

    public String getType() {
        return ExportUtils.getExportType(model.getImage());
    }
}
