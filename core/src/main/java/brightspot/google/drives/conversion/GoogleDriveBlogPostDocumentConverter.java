package brightspot.google.drives.conversion;

import brightspot.blog.BlogPage;
import brightspot.blog.BlogPostPage;
import brightspot.blog.HasBlogWithFieldData;
import brightspot.google.drive.GoogleDriveUtilsExtended;
import brightspot.google.drive.docs.DocumentConverter;
import brightspot.google.drive.docs.GoogleDocumentImport;
import brightspot.rte.importer.DefaultRichTextImporter;
import brightspot.rte.processor.DefaultRichTextImporterConfiguration;
import com.psddev.dari.db.Recordable;
import com.psddev.google.drive.GoogleDriveFile;
import org.jsoup.nodes.Element;

/**
 * Converts a {@link GoogleDriveFile} to a {@link BlogPostPage} content type.
 */
@Recordable.DisplayName("Blog Post")
public class GoogleDriveBlogPostDocumentConverter extends DocumentConverter<BlogPostPage> {

    @Required
    private BlogPage blog;

    public BlogPage getBlog() {
        return blog;
    }

    public void setBlog(BlogPage blog) {
        this.blog = blog;
    }

    @Override
    public BlogPostPage createType() {
        return new BlogPostPage();
    }

    @Override
    public void convert(GoogleDocumentImport content, GoogleDriveFile fileContext) {
        BlogPostPage blogPost = (BlogPostPage) content;
        blogPost.as(HasBlogWithFieldData.class).setBlog(getBlog());
        blogPost.setHeadline(fileContext.getName());
        blogPost.setBody(processHtml(GoogleDriveUtilsExtended.getHtml(fileContext)));
    }

    @Override
    public void update(GoogleDocumentImport content, GoogleDriveFile fileContext) {
        BlogPostPage existingObject = content.as(BlogPostPage.class);
        existingObject.setHeadline(fileContext.getName());

        String body = processHtml(GoogleDriveUtilsExtended.getHtml(fileContext));
        existingObject.setBody(body);
    }

    private String processHtml(Element body) {
        DefaultRichTextImporter importer = new DefaultRichTextImporter(new DefaultRichTextImporterConfiguration());

        return importer.processRichText(body.html());
    }
}
