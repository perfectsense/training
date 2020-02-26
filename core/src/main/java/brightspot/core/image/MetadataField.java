package brightspot.core.image;

interface MetadataField {

    String getDirectoryName();

    String getFieldName();

    default String getMetadataFieldKey() {
        return getDirectoryName() + "/" + getFieldName().replace("/", "//");
    }
}
