package training;

import brightspot.core.image.Image;
import brightspot.core.image.OneOffImageOption;
import com.psddev.dari.web.WebRequest;

public class OneOffImageRefViewModel extends RefViewModel<OneOffImageOption> {

    public String getRef() {
        Image savedImage = ExportUtils.saveOneOffImage(model);
        WebRequest.getCurrent().as(ExportRefsWebExtension.class).addRef(savedImage);
        return savedImage.getId().toString();
    }
}
