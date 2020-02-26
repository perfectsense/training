package brightspot.core.image;

enum InteroperabilityDirectory implements MetadataField {

    VERSION("Interoperability Version"),
    INDEX("Interoperability Index");

    private final String fieldName;

    InteroperabilityDirectory(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getDirectoryName() {
        return "Interoperability";
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
