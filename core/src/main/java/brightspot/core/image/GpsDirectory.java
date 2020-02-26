package brightspot.core.image;

enum GpsDirectory implements MetadataField {

    GPS_VERSION_ID("GPS Version ID"),
    GPS_LATITUDE("GPS Latitude"),
    GPS_LATITUDE_REF("GPS Latitude Ref"),
    GPS_LONGITUDE("GPS Longitude"),
    GPS_LONGITUDE_REF("GPS Longitude Ref"),
    GPS_TIME_STAMP("GPS Time-Stamp");

    private final String fieldName;

    GpsDirectory(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getDirectoryName() {
        return "GPS";
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
