package brightspot.core.image;

enum ExifSubIfdDirectory implements MetadataField {

    LENS_MODEL("Lens Model"),
    EXIF_VERSION("Exif Version"),
    COMPONENTS_CONFIGURATION("Components Configuration"),
    FLASH_PIX_VERSION("FlashPix Version"),
    FOCAL_PLANE_X_RESOLUTION("Focal Plane X Resolution"),
    SUB_SEC_TIME_ORIGINAL("Sub-Sec Time Original"),
    FOCAL_LENGTH("Focal Length"),
    SUBJECT_DISTANCE("Subject Distance"),
    SUB_SEC_TIME_DIGITIZED("Sub-Sec Time Digitized"),
    DATE_TIME_ORIGINAL("Date/Time Original"),
    SHUTTER_SPEED_VALUE("Shutter Speed Value"),
    FOCAL_PLANE_RESOLUTION_UNIT("Focal Plane Resolution Unit"),
    GAMMA("Gamma"),
    COLOR_SPACE("Color Space"),
    F_NUMBER("F-Number"),
    DATE_TIME_DIGITIZED("Date/Time Digitized"),
    SENSITIVITY_TYPE("Sensitivity Type"),
    EXPOSURE_PROGRAM("Exposure Program"),
    FLASH("Flash"),
    APERTURE_VALUE("Aperture Value"),
    USER_COMMENT("User Comment"),
    EXPOSURE_TIME("Exposure Time"),
    ISO_SPEED_RATINGS("ISO Speed Ratings"),
    RECOMMENDED_EXPOSURE_INDEX("Recommended Exposure Index"),
    EXIF_IMAGE_WIDTH("Exif Image Width"),
    METERING_MODE("Metering Mode"),
    BODY_SERIAL_NUMBER("Body Serial Number"),
    EXPOSURE_BIAS_VALUE("Exposure Bias Value"),
    FOCAL_PLANE_Y_RESOLUTION("Focal Plane Y Resolution"),
    CAMERA_OWNER_NAME("Camera Owner Name"),
    LENS_SPECIFICATION("Lens Specification"),
    LENS_SERIAL_NUMBER("Lens Serial Number"),
    MAX_APERTURE_VALUE("Max Aperture Value"),
    EXPOSURE_MODE("Exposure Mode"),
    SCENE_CAPTURE_TYPE("Scene Capture Type"),
    CUSTOM_RENDERED("Custom Rendered"),
    EXIF_IMAGE_HEIGHT("Exif Image Height"),
    SUB_SEC_TIME("Sub-Sec Time"),
    WHITE_BALANCE_MODE("White Balance Mode");

    private final String fieldName;

    ExifSubIfdDirectory(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getDirectoryName() {
        return "Exif SubIFD";
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
