package brightspot.image;

import brightspot.article.ArticleLead;
import brightspot.article.ListicleLead;
import brightspot.blog.BlogPostLead;
import brightspot.liveblog.LiveBlogLead;
import brightspot.pressrelease.PressReleaseLead;
import com.psddev.dari.util.Substitution;

public class ImageLeadSubstitution extends ImageLead implements
        ArticleLead,
        BlogPostLead,
        ListicleLead,
        LiveBlogLead,
        PressReleaseLead,
        Substitution {

    @Override
    public WebImage getArticleLeadImage() {
        return getWebImage();
    }

    @Override
    public WebImageAsset getBlogPostLeadImage() {
        return getWebImage();
    }

    @Override
    public WebImageAsset getListicleLeadImage() {
        return getWebImage();
    }

    @Override
    public WebImageAsset getLiveBlogLeadImage() {
        return getWebImage();
    }

    @Override
    public WebImageAsset getPressReleaseLeadImage() {
        return getWebImage();
    }
}
