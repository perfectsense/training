package brightspot.google.drives.conversion;

import brightspot.article.Article;
import brightspot.google.drive.GoogleDriveUtilsExtended;
import brightspot.google.drive.docs.DocumentConverter;
import brightspot.google.drive.docs.GoogleDocumentImport;
import brightspot.rte.importer.DefaultRichTextImporter;
import brightspot.rte.processor.DefaultRichTextImporterConfiguration;
import com.psddev.dari.db.Recordable;
import com.psddev.google.drive.GoogleDriveFile;
import org.jsoup.nodes.Element;

/**
 * Converts a {@link GoogleDriveFile} to an {@link Article} content type.
 */
@Recordable.DisplayName("Article")
public class GoogleDriveArticleDocumentConverter extends DocumentConverter<Article> {

    @Override
    public Article createType() {
        return new Article();
    }

    @Override
    public void convert(GoogleDocumentImport content, GoogleDriveFile fileContext) {
        Article article = (Article) content;
        article.setHeadline(fileContext.getName());
        article.setBody(processHtml(GoogleDriveUtilsExtended.getHtml(fileContext)));
    }

    @Override
    public void update(GoogleDocumentImport content, GoogleDriveFile fileContext) {
        Article existingObject = content.as(Article.class);
        existingObject.setHeadline(fileContext.getName());

        String body = processHtml(GoogleDriveUtilsExtended.getHtml(fileContext));
        existingObject.setBody(body);
    }

    private String processHtml(Element body) {
        DefaultRichTextImporter importer = new DefaultRichTextImporter(new DefaultRichTextImporterConfiguration());

        return importer.processRichText(body.html());
    }
}
