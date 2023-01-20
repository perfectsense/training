package brightspot.article;

import brightspot.image.WebImageAsset;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public interface ListicleLead extends Recordable {

    WebImageAsset getListicleLeadImage();
}
