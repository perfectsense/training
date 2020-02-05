package brightspot.core.image;

enum JpegDirectory implements MetadataField {

    NUMBER_OF_COMPONENTS("Number of Components"),
    COMPONENT_1("Component 1"),
    COMPONENT_2("Component 2"),
    COMPONENT_3("Component 3"),
    COMPRESSION_TYPE("Compression Type"),
    DATA_PRECISION("Data Precision"),
    IMAGE_HEIGHT("Image Height"),
    IMAGE_WIDTH("Image Width");

    private final String fieldName;

    JpegDirectory(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getDirectoryName() {
        return "JPEG";
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
