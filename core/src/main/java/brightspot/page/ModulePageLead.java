package brightspot.page;

import brightspot.image.WebImageAsset;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public interface ModulePageLead extends Recordable {

    WebImageAsset getModulePageLeadImage();
}
