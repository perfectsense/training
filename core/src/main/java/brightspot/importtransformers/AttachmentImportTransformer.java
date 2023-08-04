package brightspot.importtransformers;

import brightspot.attachment.Attachment;
import brightspot.dam.asset.AssetType;
import brightspot.dam.attachmentfile.AttachmentFile;
import brightspot.dam.document.Document;
import brightspot.dam.document.DocumentAssetType;
import brightspot.dam.presentation.Presentation;
import brightspot.dam.presentation.PresentationAssetType;
import brightspot.dam.spreadsheet.Spreadsheet;
import brightspot.dam.spreadsheet.SpreadsheetAssetType;
import brightspot.importapi.ImportObjectModification;
import brightspot.importapi.ImportTransformer;
import brightspot.importtransformers.element.ImportElementUtil;
import com.google.common.base.Preconditions;
import com.psddev.dari.db.Predicate;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import org.apache.commons.lang3.StringUtils;

public class AttachmentImportTransformer extends ImportTransformer<Attachment> {

    private String url;

    private String title;

    private String description;

    @Override
    public Attachment transform() throws Exception {

        Preconditions.checkArgument(
            StringUtils.isNotBlank(this.getUrl()),
            "url for Attachment with externalId [" + this.getExternalId() + "]");

        String fileUrl = ImportTransformerUtil.prependFileBaseUrlIfNeeded(this.getUrl(), this.getFileBaseUrl());

        StorageItem file = createStorageItemFromUrl(fileUrl);

        String mimeType = file.getContentType();
        if (StringUtils.isBlank(mimeType)) {
            throw new IllegalArgumentException(
                "No mimeType parsed from path [" + file.getPath() + "] for Attachment with externalId ["
                    + this.getExternalId() + "]");
        }

        AssetType assetType = Query.from(AssetType.class)
            .where("mimeTypes = ?", mimeType)
            .first();
        if (assetType == null) {
            throw new IllegalArgumentException(
                "No AssetType matching mimeType [" + mimeType + "] for Attachment with externalId ["
                    + this.getExternalId() + "]");
        }

        String processedTitle = ImportElementUtil.processInlineRichText(this.getTitle(), this);
        String processedDescription = ImportElementUtil.processInlineRichText(this.getDescription(), this);

        Attachment attachment = null;

        if (assetType instanceof DocumentAssetType) {

            Document document = new Document();
            document.setFile(file);
            document.setAssetType((DocumentAssetType) assetType);
            document.setTitle(processedTitle);
            document.setDescription(processedDescription);
            attachment = document;

        } else if (assetType instanceof SpreadsheetAssetType) {

            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.setFile(file);
            spreadsheet.setAssetType((SpreadsheetAssetType) assetType);
            spreadsheet.setTitle(processedTitle);
            spreadsheet.setDescription(processedDescription);
            attachment = spreadsheet;

        } else if (assetType instanceof PresentationAssetType) {

            Presentation presentation = new Presentation();
            presentation.setFile(file);
            presentation.setAssetType((PresentationAssetType) assetType);
            presentation.setTitle(processedTitle);
            presentation.setDescription(processedDescription);
            attachment = presentation;

        } else {

            AttachmentFile attachmentFile = new AttachmentFile();
            attachmentFile.setFile(file);
            attachmentFile.setTitle(processedTitle);
            attachmentFile.setDescription(processedDescription);
            attachment = attachmentFile;

        }

        attachment.as(ImportObjectModification.class).setSourceUrl(fileUrl);

        return attachment;
    }

    @Override
    public Predicate getUniqueFieldPredicate() {
        if (StringUtils.isBlank(this.getUrl())) {
            return null;
        }
        return PredicateParser.Static.parse(
            ImportObjectModification.SOURCE_URL_FIELD_NAME + " = \"" + this.getUrl() + "\"");
    }

    @Override
    public String getExternalId() {
        return ObjectUtils.firstNonBlank(this.externalId, this.getIdAsString(), this.getUrl());
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
