package brightspot.image;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import brightspot.google.drive.GoogleDriveImport;
import brightspot.imageitemstream.WebImageItem;
import brightspot.microsoft.drives.MicrosoftDrivesImport;
import brightspot.rss.feed.apple.RssWebImage;
import brightspot.rte.image.ImageRichTextElement;
import com.google.common.collect.ImmutableList;
import com.psddev.cms.db.Interchangeable;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.Substitution;

public class WebImageSubstitution extends WebImage implements
    GoogleDriveImport,
    Interchangeable,
    MicrosoftDrivesImport,
    RssWebImage,
    Substitution {

    // --- GoogleDriveImport support ---

    @Override
    public String getDefaultGoogleDriveExtension() {
        return ".jpg";
    }

    @Override
    public void setFileFromGoogleDrive(StorageItem file) {
        setFile(file);
    }

    @Override
    public void setTitleFromGoogleDrive(String title) {
        setCaptionOverride(title);
    }

    @Override
    public boolean supportsGoogleDriveImport(String mimeType) {
        return Optional.ofNullable(getState().getType().getField("file"))
            .filter(field -> field.getMimeTypes() != null && field.getMimeTypes().contains(mimeType))
            .isPresent();
    }

    // --- Interchangeable support ---

    @Override
    public boolean loadTo(Object target) {

        // Support the following types for Shelf drag and drop:
        // - WebImageItem (Gallery slide / Advanced type)
        // - ImageRichTextElement
        if (target instanceof WebImageItem) {

            WebImageItem webImageItem = (WebImageItem) target;
            webImageItem.setItem(this);

            return true;

        } else if (target instanceof ImageRichTextElement) {

            ImageRichTextElement imageRte = (ImageRichTextElement) target;
            imageRte.setImage(this);

            return true;
        }
        return false;
    }

    @Override
    public List<UUID> loadableTypes(Recordable recordable) {

        // Support the following types for Shelf drag and drop:
        // - WebImageItem (Gallery slide / Advanced type)
        // - ImageRichTextElement
        return ImmutableList.of(
            getState().getTypeId(), // enable dragging and dropping as itself from the shelf
            ObjectType.getInstance(WebImageItem.class).getId(),
            ObjectType.getInstance(ImageRichTextElement.class).getId()
        );
    }

    // --- MicrosoftDriveImport support ---

    @Override
    public String getDefaultMicrosoftDriveExtension() {
        return ".jpg";
    }

    @Override
    public void setFileFromMicrosoftDrive(StorageItem file) {
        setFile(file);
    }

    @Override
    public void setTitleFromMicrosoftDrive(String title) {
        setCaptionOverride(title);
    }

    @Override
    public boolean supportsMicrosoftDriveImport(String mimeType) {
        return Optional.ofNullable(getState().getType().getField("file"))
            .filter(field -> field.getMimeTypes() != null && field.getMimeTypes().contains(mimeType))
            .isPresent();
    }

    // --- RssWebImage support ---

    @Override
    public Integer getRssWebImageWidth() {
        return getWidth();
    }

    @Override
    public Integer getRssWebImageHeight() {
        return getHeight();
    }

    @Override
    public StorageItem getRssWebImageAssetFile() {
        return getFile();
    }
}
