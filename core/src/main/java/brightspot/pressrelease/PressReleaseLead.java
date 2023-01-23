package brightspot.pressrelease;

import brightspot.image.WebImageAsset;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public interface PressReleaseLead extends Recordable {

    WebImageAsset getPressReleaseLeadImage();
}
