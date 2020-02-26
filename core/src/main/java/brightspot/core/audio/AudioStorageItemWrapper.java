package brightspot.core.audio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Map;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;

/**
 * A wrapper class for an Audio Storage Item. Files uploaded through class are restricted to just audio files.
 */
@Recordable.Embedded
@Recordable.DisplayName("Audio File")
public class AudioStorageItemWrapper extends Record {

    @Required
    @MimeTypes("+audio/")
    private StorageItem file;

    @ToolUi.Hidden
    private Long duration;

    public StorageItem getFile() {
        return file;
    }

    public void setFile(StorageItem file) {
        this.file = file;
    }

    public Long getDuration() {
        if (duration == null) {
            try {
                // TODO: support mp3 duration
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(file.getData()));
                AudioFormat format = audioInputStream.getFormat();
                duration = (long) (1000 * audioInputStream.getFrameLength() / format.getFrameRate());
            } catch (IOException | UnsupportedAudioFileException | NullPointerException e) {
                // Do nothing
            }
        }
        return duration;
    }

    public String getMimeType() {
        return file != null ? file.getContentType() : null;
    }

    @Override
    public String getLabel() {
        String label = null;
        if (file != null && file.getContentType() != null) {
            Map<String, Object> metadata = file.getMetadata();

            if (metadata != null) {
                label = ObjectUtils.to(String.class, metadata.get("originalFilename"));
            }

            if (label == null) {
                label = file.getContentType();
            }
        }

        return label;
    }
}
