package brightspot.core.image;

enum IccProfileDirectory implements MetadataField {

    MEDIA_BLACK_POINT("Media Black Point"),
    GREEN_TRC("Green TRC"),
    CMM_TYPE("CMM Type"),
    XYZ_VALUES("XYZ values"),
    GREEN_COLORANT("Green Colorant"),
    PROFILE_DESCRIPTION("Profile Description"),
    BLUE_TRC("Blue TRC"),
    RED_COLORANT("Red Colorant"),
    TAG_COUNT("Tag Count"),
    PROFILE_SIZE("Profile Size"),
    BLUE_COLORANT("Blue Colorant"),
    COPYRIGHT("Copyright"),
    DEVICE_MANUFACTURER("Device manufacturer"),
    VERSION("Version"),
    COLOR_SPACE("Color space"),
    SIGNATURE("Signature"),
    CLASS("Class"),
    PROFILE_DATE_TIME("Profile Date/Time"),
    PROFILE_CONNECTION_SPACE("Profile Connection Space"),
    MEDIA_WHITE_POINT("Media White Point"),
    PRIMARY_PLATFORM("Primary Platform"),
    RED_TRC("Red TRC");

    private final String fieldName;

    IccProfileDirectory(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getDirectoryName() {
        return "ICC Profile";
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
