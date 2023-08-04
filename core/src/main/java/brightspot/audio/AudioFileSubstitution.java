package brightspot.audio;

import java.util.Collections;

import brightspot.audio.file.AudioFile;
import brightspot.audio.file.AudioStorageItemWrapper;
import brightspot.google.drive.GoogleDriveImport;
import brightspot.microsoft.drives.MicrosoftDrivesImport;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.Substitution;

public class AudioFileSubstitution extends AudioFile implements
    Substitution,
    GoogleDriveImport,
    MicrosoftDrivesImport {

    // --- GoogleDriveImport support ---

    @Override
    public String getDefaultGoogleDriveExtension() {
        return ".m4a";
    }

    @Override
    public void setFileFromGoogleDrive(StorageItem file) {
        AudioStorageItemWrapper storageItemWrapper = new AudioStorageItemWrapper();
        storageItemWrapper.setFile(file);

        setItems(Collections.singletonList(storageItemWrapper));
    }

    @Override
    public void setTitleFromGoogleDrive(String title) {
        this.as(AudioData.class).setTitleOverride(title);
    }

    @Override
    public boolean supportsGoogleDriveImport(String mimeType) {
        return ObjectType.getInstance(AudioStorageItemWrapper.class).getFields().stream()
            .anyMatch(field -> field.getMimeTypes() != null && field.getMimeTypes().contains(mimeType));
    }

    // --- MicrosoftDriveImport support ---

    @Override
    public String getDefaultMicrosoftDriveExtension() {
        return ".m4a";
    }

    @Override
    public void setFileFromMicrosoftDrive(StorageItem file) {
        AudioStorageItemWrapper storageItemWrapper = new AudioStorageItemWrapper();
        storageItemWrapper.setFile(file);

        setItems(Collections.singletonList(storageItemWrapper));
    }

    @Override
    public void setTitleFromMicrosoftDrive(String title) {
        this.as(AudioData.class).setTitleOverride(title);
    }

    @Override
    public boolean supportsMicrosoftDriveImport(String mimeType) {
        return ObjectType.getInstance(AudioStorageItemWrapper.class).getFields().stream()
            .anyMatch(field -> field.getMimeTypes() != null && field.getMimeTypes().contains(mimeType));
    }
}
