package brightspot.google.drives.conversion;

import brightspot.google.drive.GoogleDriveUtilsExtended;
import brightspot.google.drive.docs.DocumentConverter;
import brightspot.google.drive.docs.GoogleDocumentImport;
import brightspot.pressrelease.PressRelease;
import brightspot.rte.importer.DefaultRichTextImporter;
import brightspot.rte.processor.DefaultRichTextImporterConfiguration;
import com.psddev.dari.db.Recordable;
import com.psddev.google.drive.GoogleDriveFile;
import org.jsoup.nodes.Element;

/**
 * Converts a {@link GoogleDriveFile} to a {@link PressRelease} content type.
 */
@Recordable.DisplayName("Press Release")
public class GoogleDrivePressReleaseDocumentConverter extends DocumentConverter<PressRelease> {

    @Override
    public PressRelease createType() {
        return new PressRelease();
    }

    @Override
    public void convert(GoogleDocumentImport content, GoogleDriveFile fileContext) {
        PressRelease pressRelease = (PressRelease) content;
        pressRelease.setHeadline(fileContext.getName());
        pressRelease.setBody(processHtml(GoogleDriveUtilsExtended.getHtml(fileContext)));
    }

    @Override
    public void update(GoogleDocumentImport content, GoogleDriveFile fileContext) {
        PressRelease existingObject = content.as(PressRelease.class);
        existingObject.setHeadline(fileContext.getName());

        String body = processHtml(GoogleDriveUtilsExtended.getHtml(fileContext));
        existingObject.setBody(body);
    }

    private String processHtml(Element body) {
        DefaultRichTextImporter importer = new DefaultRichTextImporter(new DefaultRichTextImporterConfiguration());

        return importer.processRichText(body.html());
    }
}
