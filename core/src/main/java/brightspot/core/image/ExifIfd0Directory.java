package brightspot.core.image;

enum ExifIfd0Directory implements MetadataField {

    ARTIST("Artist"),
    DATE_TIME("Date/Time"),
    MAKE("Make"),
    SAMPLES_PER_PIXEL("Samples Per Pixel"),
    YCBCR_POSITIONING("YCbCr Positioning"),
    IMAGE_HEIGHT("Image Height"),
    ORIENTATION("Orientation"),
    WHITE_POINT("White Point"),
    X_RESOLUTION("X Resolution"),
    IMAGE_WIDTH("Image Width"),
    PRIMARY_CHROMATICITIES("Primary Chromaticities"),
    YCBCR_COEFFICIENTS("YCbCr Coefficients"),
    PHOTOMETRIC_INTERPRETATION("Photometric Interpretation"),
    RESOLUTION_UNIT("Resolution Unit"),
    MODEL("Model"),
    SOFTWARE("Software"),
    BITS_PER_SAMPLE("Bits Per Sample"),
    Y_RESOLUTION("Y Resolution");

    private final String fieldName;

    ExifIfd0Directory(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getDirectoryName() {
        return "Exif IFD0";
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
