package brightspot.blog;

import brightspot.image.WebImageAsset;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public interface BlogPostLead extends Recordable {

    WebImageAsset getBlogPostLeadImage();
}
