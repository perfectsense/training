package brightspot.image;

import brightspot.article.ArticleLead;
import com.psddev.dari.util.Substitution;

public class ImageLeadSubstitution extends ImageLead implements
        ArticleLead,
        Substitution {

    @Override
    public WebImage getArticleLeadImage() {
        return getWebImage();
    }
}
