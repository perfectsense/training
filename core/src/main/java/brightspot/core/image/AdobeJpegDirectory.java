package brightspot.core.image;

enum AdobeJpegDirectory implements MetadataField {

    COLOR_TRANSFORM("Color Transform"),
    DCT_ENCODE_VERSION("DCT Encode Version"),
    FLAGS_0("Flags 0"),
    FLAGS_1("Flags 1");

    private final String fieldName;

    AdobeJpegDirectory(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getDirectoryName() {
        return "Adobe JPEG";
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
