package brightspot.core.lead;

import brightspot.core.image.ImageOption;
import com.psddev.dari.db.Recordable;

public interface Lead extends Recordable {

    ImageOption getLeadImage();
}
