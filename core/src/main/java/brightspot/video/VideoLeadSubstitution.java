package brightspot.video;

import brightspot.article.ArticleLead;
import brightspot.article.ListicleLead;
import brightspot.blog.BlogPostLead;
import brightspot.image.WebImageAsset;
import brightspot.liveblog.LiveBlogLead;
import brightspot.pressrelease.PressReleaseLead;
import com.psddev.dari.util.Substitution;

public class VideoLeadSubstitution extends VideoLead implements
    ArticleLead,
    BlogPostLead,
    ListicleLead,
    LiveBlogLead,
    PressReleaseLead,
    Substitution {

    @Override
    public WebImageAsset getArticleLeadImage() {
        return getVideoPosterImage();
    }

    @Override
    public WebImageAsset getBlogPostLeadImage() {
        return getVideoPosterImage();
    }

    @Override
    public WebImageAsset getListicleLeadImage() {
        return getVideoPosterImage();
    }

    @Override
    public WebImageAsset getLiveBlogLeadImage() {
        return getVideoPosterImage();
    }

    @Override
    public WebImageAsset getPressReleaseLeadImage() {
        return getVideoPosterImage();
    }
}
