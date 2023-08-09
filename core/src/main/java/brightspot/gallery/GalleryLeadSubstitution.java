package brightspot.gallery;

import brightspot.article.ArticleLead;
import brightspot.article.ListicleLead;
import brightspot.blog.BlogPostLead;
import brightspot.image.WebImageAsset;
import brightspot.liveblog.LiveBlogLead;
import brightspot.pressrelease.PressReleaseLead;
import com.psddev.dari.util.Substitution;

public class GalleryLeadSubstitution extends GalleryLead implements ArticleLead, BlogPostLead, ListicleLead,
    LiveBlogLead, PressReleaseLead, Substitution {

    @Override
    public WebImageAsset getArticleLeadImage() {
        return getFirstImage();
    }

    @Override
    public WebImageAsset getListicleLeadImage() {
        return getFirstImage();
    }

    @Override
    public WebImageAsset getBlogPostLeadImage() {
        return getFirstImage();
    }

    @Override
    public WebImageAsset getLiveBlogLeadImage() {
        return getFirstImage();
    }

    @Override
    public WebImageAsset getPressReleaseLeadImage() {
        return getFirstImage();
    }
}
