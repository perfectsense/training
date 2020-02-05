package brightspot.core.image;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.core.asset.AssetMimeTypesProvider;

public class ImageMimeTypesProvider implements AssetMimeTypesProvider {

    @Override
    public Set<String> get() {
        // TODO: Need to be able to "substitute" some core types w/ instances of ImageFormat in DAM.
        return Stream.concat(
            Stream.of(Image.VIEWABLE_MIME_TYPES),
            Stream.of(
                "image/iff",
                "image/x-iff",
                "image/x-portable-anymap",
                "image/x-portable-arbitrarymap",
                "image/x-portable-pixmap",
                "image/tiff",
                "image/x-tiff",
                "application/postscript",
                "image/x-adobe-dng",
                "image/vnd.adobe.photoshop",
                "application/photoshop",
                "application/psd",
                "application/x-photoshop",
                "image/psd"))
            .collect(Collectors.toSet());
    }
}
