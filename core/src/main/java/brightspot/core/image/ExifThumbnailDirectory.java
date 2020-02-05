package brightspot.core.image;

enum ExifThumbnailDirectory implements MetadataField {

    RESOLUTION_UNIT("Resolution Unit"),
    LENGTH("Thumbnail Length"),
    OFFSET("Thumbnail Offset"),
    COMPRESSION("Thumbnail Compression"),
    X_RESOLUTION("X Resolution"),
    Y_RESOLUTION("Y Resolution");

    private final String fieldName;

    ExifThumbnailDirectory(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getDirectoryName() {
        return "Exif Thumbnail";
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
