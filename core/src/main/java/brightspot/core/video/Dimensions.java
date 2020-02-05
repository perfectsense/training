package brightspot.core.video;

/**
 * Represents the width and height of a video.
 */
public class Dimensions {

    private Integer width;
    private Integer height;

    public static Dimensions of(Integer width, Integer height) {

        if (width == null || width <= 0 || height == null || height <= 0) {
            return null;
        }

        return new Dimensions(width, height);
    }

    public static Dimensions ofChecked(Integer width, Integer height) {

        // we allow one of the dimensions to be null since that can sometimes
        // be enough to still render or scale the object.
        if (width == null || width <= 0 || height == null || height <= 0) {
            throw new IllegalArgumentException("Both the width and height cannot be null!");
        }

        return new Dimensions(width, height);
    }

    public Dimensions() {

    }

    public Dimensions(Integer width, Integer height) {

        this.width = width;
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Double getAspectRatio() {
        return width != null && height != null ? (double) width / (double) height : null;
    }

    public String getAspectRatioLabel() {
        if (width == null || height == null) {
            return "N/A";
        }
        long gcd = gcd(width, height);
        return (width / gcd) + ":" + (height / gcd);
    }

    public String getDimensionsLabel() {
        if (width == null && height == null) {
            return null;
        }
        return (width != null ? width : "?") + "x" + (height != null ? height : "?");
    }

    // http://stackoverflow.com/questions/4201860/how-to-find-gcd-lcm-on-a-set-of-numbers
    private static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }
}
