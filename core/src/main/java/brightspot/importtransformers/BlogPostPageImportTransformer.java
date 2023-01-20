package brightspot.importtransformers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.author.Author;
import brightspot.author.PersonAuthor;
import brightspot.blog.BlogPage;
import brightspot.blog.BlogPostPage;
import brightspot.image.ImageLead;
import brightspot.image.ImageLeadSubstitution;
import brightspot.image.WebImage;
import brightspot.importapi.ImportTransformer;
import brightspot.importapi.ImportingDatabase;
import brightspot.importtransformers.richtext.BodyElement;
import brightspot.importtransformers.richtext.BodyTransformUtil;
import brightspot.tag.TagPage;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlogPostPageImportTransformer extends ImportTransformer<BlogPostPage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BlogPostPageImportTransformer.class);

    private String headline;

    private String subheadline;

    private String leadUrl;

    private String blog;

    private List<BodyElement> body;

    private Set<String> authors;

    private Set<String> tags;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSubheadline() {
        return subheadline;
    }

    public void setSubheadline(String subheadline) {
        this.subheadline = subheadline;
    }

    public String getLeadUrl() {
        return leadUrl;
    }

    public void setLeadUrl(String leadUrl) {
        this.leadUrl = leadUrl;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blogPage) {
        this.blog = blogPage;
    }

    public List<BodyElement> getBody() {
        if (body == null) {
            body = new ArrayList<>();
        }
        return body;
    }

    public void setBody(List<BodyElement> body) {
        this.body = body;
    }

    public Set<String> getAuthors() {
        if (authors == null) {
            authors = new HashSet<>();
        }
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }

    public Set<String> getTags() {
        if (tags == null) {
            tags = new HashSet<>();
        }
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    @Override
    public BlogPostPage transform() throws Exception {

        BlogPostPage blogPostPage = new BlogPostPage();

        blogPostPage.setHeadline(headline);
        blogPostPage.setSubheadline(subheadline);

        Optional.ofNullable(this.getBody())
                .map(b -> BodyTransformUtil.transform(body, this))
                .ifPresent(blogPostPage::setBody);

        if (!StringUtils.isBlank(leadUrl)) {
            WebImage image = findByExternalId(WebImage.class, leadUrl, (url) -> {
                try {
                    WebImage w = new WebImage();
                    w.setFile(createStorageItemFromUrl(url));
                    return w;
                } catch (IOException ex) {
                    return null;
                }
            });
            if (image != null) {
                ImageLead imageLead = new ImageLead();
                imageLead.setImage(image);
                blogPostPage.setLead(imageLead.as(ImageLeadSubstitution.class));
            }
        }

        Site site = ((ImportingDatabase) Database.Static.getDefault()).getSite();

        if (blog != null) {
            BlogPage blog = findByExternalId(BlogPage.class, this.blog, (bp) -> {
                String displayName = Utils.toLabel(bp);
                BlogPage b = Query.from(BlogPage.class).where("displayName = ?", displayName)
                        .and("cms.site.owner = ?", site).first();
                if (b == null) {
                    b = new BlogPage();
                    b.setDisplayName(displayName);
                }
                return b;
            });
            blogPostPage.asHasBlogWithFieldData().setBlog(blog);
        }

        if (authors != null) {
            List<Author> authorList = new ArrayList<>();
            for (String authorName : authors) {
                PersonAuthor author = findByExternalId(PersonAuthor.class, authorName, (an) -> {
                    String displayName = Utils.toLabel(an);
                    PersonAuthor a = Query.from(PersonAuthor.class).where("name = ?", displayName)
                            .and("cms.site.owner = ?", site).first();
                    if (a == null) {
                        a = new PersonAuthor();
                        a.setName(displayName);
                    }
                    return a;
                });
                authorList.add(author);
            }
            blogPostPage.asHasAuthorsWithFieldData().setAuthors(authorList);
        }

        if (tags != null) {
            for (String tagName : tags) {
                TagPage tag = findByExternalId(TagPage.class, tagName, (tn) -> {
                    String displayName = Utils.toLabel(tn);
                    TagPage t = Query.from(TagPage.class).where("tag.getTagDisplayNamePlainText = ?", displayName)
                            .and("cms.site.owner = ?", site).first();
                    if (t == null) {
                        t = new TagPage();
                        t.setDisplayName(displayName);
                    }
                    return t;
                });
                blogPostPage.getTags().add(tag);
            }
        }

        return blogPostPage;
    }
}
