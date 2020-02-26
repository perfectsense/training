package brightspot.core.image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.psddev.dari.db.Location;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Parses out the IPTC/EXIF/GPS information from a an image StorageItem's metadata and exposes it in convenient to use
 * APIs.
 *
 * NOTE: These mappings depend on version 2.8.1 of the drewnoakes image extraction library.
 */
public class StorageItemImageMetadata {

    /* FILE NAME */
    private static final String ORIGINAL_FILENAME_KEY = "originalFilename";
    private static final String ORIGINAL_FILE_PATH_KEY = "originalFilePath";

    /* DIMENSIONS */
    private static final String WIDTH_KEY = "width"; // 1600
    private static final String HEIGHT_KEY = "height"; // 1200

    private static final String ORIGINAL_WIDTH_KEY = "originalWidth";
    private static final String ORIGINAL_HEIGHT_KEY = "originalHeight";

    /* HTTP HEADERS */
    private static final String HTTP_HEADER_CACHE_CONTROL_KEY = "http.headers/Cache-Control"; // [ "public, max-age=31536000" ]
    private static final String HTTP_HEADER_CONTENT_LENGTH_KEY = "http.headers/Content-Length"; // [ "99360" ]
    private static final String HTTP_HEADER_CONTENT_TYPE_KEY = "http.headers/Content-Type"; // [ "image/jpeg" ]

    /* EXIF_ORIENTATION_KEY values */
    private static final Map<String, Integer> EXIF_IMAGE_ORIENTATIONS;

    static {
        Map<String, Integer> map = new HashMap<>();
        map.put("Top, left side (Horizontal / normal)", 1);
        map.put("Top, right side (Mirror horizontal)", 2);
        map.put("Bottom, right side (Rotate 180)", 3);
        map.put("Bottom, left side (Mirror vertical)", 4);
        map.put("Left side, top (Mirror horizontal and rotate 270 CW)", 5);
        map.put("Right side, top (Rotate 90 CW)", 6);
        map.put("Right side, bottom (Mirror horizontal and rotate 90 CW)", 7);
        map.put("Left side, bottom (Rotate 270 CW)", 8);
        EXIF_IMAGE_ORIENTATIONS = map;
    }

    private static final transient MetadataField[] EXIF_DATE_TIME_KEYS = {
        ExifSubIfdDirectory.DATE_TIME_DIGITIZED,
        ExifSubIfdDirectory.DATE_TIME_ORIGINAL,
        ExifIfd0Directory.DATE_TIME
    };

    private static final transient MetadataField[] METADATA_TITLE_KEYS = {
        IptcDirectory.HEADLINE,
        IptcDirectory.OBJECT_NAME
    };

    private static final transient MetadataField[] METADATA_BYLINE_KEYS = {
        IptcDirectory.BY_LINE,
        ExifIfd0Directory.ARTIST
    };

    /* Date-Time Format: 2014:01:22 12:12:44 */
    private static final String EXIF_DATE_TIME_FORMAT = "yyyy:MM:dd hh:mm:ss";

    /* Time Format: 121244+0000 */
    private static final String IPTC_TIME_CREATED_FORMAT = "hhmmss+SSSS";

    /* Date Format: Wed Jan 22 00:00:00 EST 2014 */
    private static final String IPTC_DATE_CREATED_FORMAT = "EEE MMM dd 00:00:00 zzz yyyy";

    private Map<String, Object> metadata;

    /**
     * TODO: Javadocs
     *
     * @param item
     */
    public StorageItemImageMetadata(StorageItem item) {
        if (item != null) {
            metadata = item.getMetadata();
        }
        if (metadata == null) {
            metadata = new HashMap<>();
            if (item != null) {
                // FIXME: Need to rethink the mutability aspect of this...
                item.setMetadata(metadata);
            }
        }
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public Integer getWidth() {
        return getValueForKey(Integer.class, WIDTH_KEY);
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public Integer getHeight() {
        return getValueForKey(Integer.class, HEIGHT_KEY);
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public String getCaption() {
        return getValueForKey(String.class, IptcDirectory.CAPTION_ABSTRACT);
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public String getTitle() {
        return getFirstNonBlankValueForKeys(String.class, METADATA_TITLE_KEYS);
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public String getOriginalFileName() {
        return getValueForKey(String.class, ORIGINAL_FILENAME_KEY);
    }

    /**
     * Returns the relative path and the filename of the original file if uploaded via directory uploader. e.g.:
     * foo/bar.jpg
     *
     * @return original file path including filename.
     */
    public String getOriginalFilePath() {
        return getValueForKey(String.class, ORIGINAL_FILE_PATH_KEY);
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public String getByline() {
        return getFirstNonBlankValueForKeys(String.class, METADATA_BYLINE_KEYS);
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public String getCredit() {
        return getValueForKey(String.class, IptcDirectory.CREDIT);
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public String getSource() {
        return getValueForKey(String.class, IptcDirectory.SOURCE);
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public String getCopyrightNotice() {
        return getValueForKey(String.class, IptcDirectory.COPYRIGHT_NOTICE);
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public List<String> getKeywords() {
        String keywords = getValueForKey(String.class, IptcDirectory.KEYWORDS);
        if (!StringUtils.isBlank(keywords)) {
            return Arrays.asList(keywords.split("[|;]"));
        }
        return new ArrayList<>();
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public Date getDateTaken() {

        DateTime dateTaken = null;

        // check the IPTC metadata fields
        String iptcDate = getValueForKey(String.class, IptcDirectory.DATE_CREATED);
        if (iptcDate != null) {
            // check the date part
            try {
                dateTaken = DateTimeFormat.forPattern(IPTC_DATE_CREATED_FORMAT).parseDateTime(iptcDate);
            } catch (RuntimeException e) {
                // do nothing
            }
            if (dateTaken != null) {
                // check the time part
                String iptcTime = getValueForKey(String.class, IptcDirectory.TIME_CREATED);
                if (iptcTime != null) {
                    LocalTime timeTaken = null;
                    try {
                        timeTaken = LocalTime.parse(iptcTime, DateTimeFormat.forPattern(IPTC_TIME_CREATED_FORMAT));
                    } catch (RuntimeException e) {
                        // do nothing
                    }
                    // apply the time to the date
                    if (timeTaken != null) {
                        dateTaken = dateTaken.withFields(timeTaken);
                    }
                }
            }
        }

        // check the exif metadata fields
        if (dateTaken == null) {
            String exifDateTime = getFirstNonBlankValueForKeys(String.class, EXIF_DATE_TIME_KEYS);
            if (exifDateTime != null) {
                try {
                    dateTaken = DateTimeFormat.forPattern(EXIF_DATE_TIME_FORMAT).parseDateTime(exifDateTime);
                } catch (RuntimeException e) {
                    // do nothing
                }
            }
        }

        return dateTaken != null ? dateTaken.toDate() : null;
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public Location getGpsLocation() {
        Double x = getGpsDegrees(
            getValueForKey(String.class, GpsDirectory.GPS_LATITUDE),
            getValueForKey(String.class, GpsDirectory.GPS_LATITUDE_REF));
        Double y = getGpsDegrees(
            getValueForKey(String.class, GpsDirectory.GPS_LONGITUDE),
            getValueForKey(String.class, GpsDirectory.GPS_LONGITUDE_REF));
        return x != null && y != null ? new Location(x, y) : null;
    }

    /**
     * TODO: Javadocs
     */
    public void setOrientation() {
        // Work some magic to deal with photos with orientation metadata.
        String orientationKey = getValueForKey(String.class, ExifIfd0Directory.ORIENTATION);
        Integer orientationValue = EXIF_IMAGE_ORIENTATIONS.get(orientationKey);

        if (orientationValue != null) {

            switch (orientationValue) {
                case 1:
                    // do nothing
                    break;
                case 2:
                    CollectionUtils.putByPath(metadata, "cms.edits/flipH", true);
                    break;
                case 3:
                    CollectionUtils.putByPath(metadata, "cms.edits/flipH", true);
                    CollectionUtils.putByPath(metadata, "cms.edits/flipV", true);
                    break;
                case 4:
                    CollectionUtils.putByPath(metadata, "cms.edits/flipV", true);
                    break;
                case 5:
                    CollectionUtils.putByPath(metadata, "cms.edits/flipH", true);
                    CollectionUtils.putByPath(metadata, "cms.edits/rotate", -90);
                    break;
                case 6:
                    CollectionUtils.putByPath(metadata, "cms.edits/rotate", 90);
                    break;
                case 7:
                    CollectionUtils.putByPath(metadata, "cms.edits/flipH", true);
                    CollectionUtils.putByPath(metadata, "cms.edits/rotate", 90);
                    break;
                case 8:
                    CollectionUtils.putByPath(metadata, "cms.edits/rotate", -90);
                    break;
                default:
                    // do nothing
            }
        }
    }

    /**
     * TODO: Javadocs
     *
     * @return
     */
    public boolean isRotated90Degrees() {
        return Math.abs(ObjectUtils.to(int.class, CollectionUtils.getByPath(metadata, "cms.edits/rotate"))) == 90;
    }

    private static Double getGpsDegrees(String dmsString, String dmsRef) {

        if (dmsString == null || dmsRef == null) {
            return null;
        }

        Double degrees = null;
        Double minutes = null;
        Double seconds = null;

        double sign = 1;

        // -39° 5' 13.2"
        String[] parts = dmsString.trim().split("\\s+");
        if (parts.length == 3) {

            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                if (!part.isEmpty() && !Character.isDigit(part.charAt(part.length() - 1))) {
                    parts[i] = part.substring(0, part.length() - 1);
                }
            }

            if (parts[0].startsWith("-")) {
                parts[0] = parts[0].substring(1);
                sign = -1;
            }

            degrees = ObjectUtils.to(Double.class, parts[0]);
            minutes = ObjectUtils.to(Double.class, parts[1]);
            seconds = ObjectUtils.to(Double.class, parts[2]);
        }

        if (degrees == null || minutes == null || seconds == null) {
            return null;
        }

        return (degrees + (minutes / 60) + (seconds / 3600)) * sign;
    }

    private <T> T getValueForKey(Class<T> returnType, MetadataField field) {
        return getValueForKey(returnType, field.getMetadataFieldKey());
    }

    /**
     * Double slashes in the path means that it's part of the key and NOT a separator.
     */
    private <T> T getValueForKey(Class<T> returnType, String path) {

        // handle double slashes
        if (path.contains("//")) {

            Object subMetaData = metadata;
            // https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html#special
            for (String pathPart : path.split("(?<!/)/(?!/)")) {

                pathPart = pathPart.replaceAll("//", "/");

                if (subMetaData instanceof Map) {
                    subMetaData = ((Map<?, ?>) subMetaData).get(pathPart);
                } else {
                    subMetaData = CollectionUtils.getByPath(subMetaData, pathPart);
                }
            }
            return ObjectUtils.to(returnType, subMetaData);

        } else {
            return ObjectUtils.to(returnType, CollectionUtils.getByPath(metadata, path));
        }
    }

    /**
     * Takes an array of fields and gets the first non-blank value.
     *
     * @see #getFirstNonBlankValueForKeys(Class, String...)
     */
    private <T> T getFirstNonBlankValueForKeys(Class<T> returnType, MetadataField... fields) {
        return getFirstNonBlankValueForKeys(
            returnType,
            Arrays.stream(fields).map(MetadataField::getMetadataFieldKey).toArray(String[]::new));
    }

    /**
     * Takes an array of paths and gets the first non-blank result of calling {@link #getValueForKey(Class, String)}.
     */
    private <T> T getFirstNonBlankValueForKeys(Class<T> returnType, String... paths) {

        T value = null;

        for (String path : paths) {
            value = getValueForKey(returnType, path);
            if (!ObjectUtils.isBlank(value)) {
                break;
            }
        }

        return value;
    }
}
