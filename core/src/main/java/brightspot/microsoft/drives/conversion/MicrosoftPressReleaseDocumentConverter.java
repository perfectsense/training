package brightspot.microsoft.drives.conversion;

import java.util.Optional;

import brightspot.microsoft.drives.MicrosoftDrivesFile;
import brightspot.microsoft.drives.MicrosoftDrivesUtils;
import brightspot.microsoft.drives.conversion.document.DocumentConverter;
import brightspot.microsoft.drives.conversion.document.MicrosoftDocumentImport;
import brightspot.pressrelease.PressRelease;
import brightspot.rte.importer.DefaultRichTextImporter;
import brightspot.rte.processor.DefaultRichTextImporterConfiguration;
import com.psddev.dari.db.Recordable;
import org.apache.commons.io.FilenameUtils;
import org.jsoup.nodes.Element;

/**
 * Converts a {@link MicrosoftDrivesFile} to a {@link PressRelease} content type.
 */
@Recordable.DisplayName("Press Release")
public class MicrosoftPressReleaseDocumentConverter extends DocumentConverter<PressRelease> {

    @Override
    public PressRelease createType() {
        return new PressRelease();
    }

    @Override
    public void convert(MicrosoftDocumentImport content, MicrosoftDrivesFile fileContext) {
        update(content, fileContext);
    }

    @Override
    public void update(MicrosoftDocumentImport content, MicrosoftDrivesFile fileContext) {
        PressRelease pressRelease = (PressRelease) content;

        String headline = Optional.ofNullable(fileContext.getName())
            .map(FilenameUtils::removeExtension)
            .orElse(null);
        pressRelease.setHeadline(headline);

        String body = processHtml(MicrosoftDrivesUtils.getHtml(fileContext));
        pressRelease.setBody(body);
    }

    // Convert img tags to bsp-image tags that can be parsed into ImageRichTextElements by the RTE
    private String processHtml(Element body) {
        DefaultRichTextImporter importer = new DefaultRichTextImporter(new DefaultRichTextImporterConfiguration());

        return importer.processRichText(body.html());
    }
}
