package brightspot.article;

import brightspot.image.WebImageAsset;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public interface ArticleLead extends Recordable {

    WebImageAsset getArticleLeadImage();
}
