package brightspot.core.asset;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;

/**
 * An AbstractAsset object encapsulates a {@link com.psddev.dari.util.StorageItem}.
 */
@Seo.TitleFields("getTitle")
public abstract class AbstractAsset extends Content implements Asset {

    @IgnoredIfEmbedded
    @ToolUi.Placeholder(dynamicText = "${content.titleFallback}", editable = true)
    private String title;

    @Required
    private StorageItem file;

    @Indexed
    @ToolUi.Tab("Metadata")
    private Set<String> keywords;

    @Indexed
    @ToolUi.Tab("Metadata")
    private Date dateUploaded;

    @Indexed
    @ToolUi.Hidden
    public String getTitle() {
        if (StringUtils.isBlank(title)) {
            return getTitleFallback();
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StorageItem getFile() {
        return file;
    }

    public void setFile(StorageItem file) {
        this.file = file;
    }

    public Set<String> getKeywords() {
        return keywords == null ? new HashSet<>() : keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    @Override
    public String getLabel() {
        return ObjectUtils.firstNonBlank(title, getFileNameFromStoragePath());
    }

    /**
     * Not for external use!
     */
    public String getTitleFallback() {
        return Optional.ofNullable(getFile())
            .map(StorageItem::getMetadata)
            .map(metadata -> ObjectUtils.firstNonBlank(
                ObjectUtils.to(String.class, metadata.get("originalFilePath")),
                ObjectUtils.to(String.class, metadata.get("originalFilename"))))
            .orElse(getFileNameFromStoragePath());
    }

    /**
     * @return Nullable.
     */
    protected String getFileNameFromStoragePath() {
        StorageItem file = getFile();

        if (file == null) {
            return null;
        }

        String path = file.getPath();

        if (path == null) {
            return null;
        }

        int lastSlashAt = path.lastIndexOf('/');
        return lastSlashAt >= 0 ? path.substring(lastSlashAt + 1) : path;
    }
}
