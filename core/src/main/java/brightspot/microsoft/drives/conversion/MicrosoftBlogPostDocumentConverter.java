package brightspot.microsoft.drives.conversion;

import java.util.Optional;

import brightspot.blog.Blog;
import brightspot.blog.BlogPostPage;
import brightspot.blog.HasBlogWithFieldData;
import brightspot.microsoft.drives.MicrosoftDrivesFile;
import brightspot.microsoft.drives.MicrosoftDrivesUtils;
import brightspot.microsoft.drives.conversion.document.DocumentConverter;
import brightspot.microsoft.drives.conversion.document.MicrosoftDocumentImport;
import brightspot.rte.importer.DefaultRichTextImporter;
import brightspot.rte.processor.DefaultRichTextImporterConfiguration;
import com.psddev.dari.db.Recordable;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.nodes.Element;

/**
 * Converts a {@link MicrosoftDrivesFile} to a {@link BlogPostPage} content type.
 */
@Recordable.DisplayName("Blog Post")
public class MicrosoftBlogPostDocumentConverter extends DocumentConverter<BlogPostPage> {

    @Required
    private Blog blog;

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    @Override
    public BlogPostPage createType() {
        return new BlogPostPage();
    }

    @Override
    public void convert(MicrosoftDocumentImport content, MicrosoftDrivesFile fileContext) {
        update(content, fileContext);
    }

    @Override
    public void update(MicrosoftDocumentImport content, MicrosoftDrivesFile fileContext) {
        BlogPostPage blogPost = (BlogPostPage) content;
        blogPost.as(HasBlogWithFieldData.class).setBlog(getBlog());
        String headline = Optional.ofNullable(fileContext.getName())
            .map(FilenameUtils::removeExtension)
            .orElse(null);
        blogPost.setHeadline(headline);

        String body = processHtml(MicrosoftDrivesUtils.getHtml(fileContext));
        blogPost.setBody(body);
    }

    // Convert img tags to bsp-image tags that can be parsed into ImageRichTextElements by the RTE
    private String processHtml(Element body) {
        DefaultRichTextImporter importer = new DefaultRichTextImporter(new DefaultRichTextImporterConfiguration());

        return importer.processRichText(body.html());
    }
}
