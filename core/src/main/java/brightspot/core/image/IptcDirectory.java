package brightspot.core.image;

enum IptcDirectory implements MetadataField {

    BY_LINE_TITLE("By-line Title"),
    CATEGORY("Category"),
    BY_LINE("By-line"),
    CAPTION_ABSTRACT("Caption/Abstract"),
    CITY("City"),
    HEADLINE("Headline"),
    KEYWORDS("Keywords"),
    OBJECT_NAME("Object Name"),
    SUPPLEMENTAL_CATEGORIES("Supplemental Category(s)"),
    ORIGINAL_TRANSMISSION_REFERENCE("Original Transmission Reference"),
    CAPTION_WRITER_EDITOR("Caption Writer/Editor"),
    CREDIT("Credit"),
    SOURCE("Source"),
    COPYRIGHT_NOTICE("Copyright Notice"),
    COUNTRY_PRIMARY_LOCATION_NAME("Country/Primary Location Name"),
    APPLICATION_RECORD_VERSION("Application Record Version"),
    URGENCY("Urgency"),
    TIME_CREATED("Time Created"),
    DATE_CREATED("Date Created");

    private final String fieldName;

    IptcDirectory(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getDirectoryName() {
        return "IPTC";
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
