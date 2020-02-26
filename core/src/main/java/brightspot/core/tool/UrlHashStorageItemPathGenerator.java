package brightspot.core.tool;

import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.common.base.Preconditions;
import com.psddev.dari.util.StorageItemPathGenerator;
import com.psddev.dari.util.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;

public class UrlHashStorageItemPathGenerator implements StorageItemPathGenerator {

    /**
     * Generate a path from a full url.
     *
     * @param url a valid {@link URL}, used in its entirety to generate a hash for the path; the filename and extension
     * are appended to a path created from the hash
     * @return a path for use with a {@link com.psddev.dari.util.StorageItem}
     * @throws UncheckedIOException if {@code url} is not a valid {@link URL}
     */
    @Override
    public String createPath(String url) {
        Preconditions.checkArgument(!StringUtils.isBlank(url));
        try {
            url = url.trim();

            String path = "";

            // use full url to generate the hash
            String hashString = DigestUtils.md5Hex(url);

            path += hashString.substring(0, 2);
            path += '/';
            path += hashString.substring(2, 4);
            path += '/';
            path += hashString.substring(4);
            path += '/';

            String urlPath = StringUtils.decodeUri(new URL(url).getPath());
            String extension = FilenameUtils.getExtension(urlPath);
            String fileName = FilenameUtils.getBaseName(urlPath);

            if (StringUtils.isBlank(fileName)) {
                extension = "";
                fileName = urlPath;
            }

            path += StringUtils.toNormalized(fileName);
            if (!StringUtils.isBlank(extension)) {
                path += '.';
                path += extension;
            }

            return path;
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
    }
}
