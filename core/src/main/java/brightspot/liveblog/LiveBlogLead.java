package brightspot.liveblog;

import brightspot.image.WebImageAsset;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public interface LiveBlogLead extends Recordable {

    WebImageAsset getLiveBlogLeadImage();
}
