package brightspot.video;

import brightspot.article.ArticleLead;
import brightspot.image.WebImageAsset;
import com.psddev.dari.util.Substitution;

public class VideoLeadSubstitution extends VideoLead implements
        ArticleLead,
        Substitution {

    @Override
    public WebImageAsset getArticleLeadImage() {
        return getVideoPosterImage();
    }
}
