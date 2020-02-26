package brightspot.google.dfp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.ad.AdSize;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * An {@link AdSizeMap} defines size restrictions for an {@link GoogleDfpAdModule}.
 */
@Recordable.Embedded
public class AdSizeMap extends Record {

    @Required
    private WindowSize minimumWindowSize;

    @CollectionMinimum(1)
    private List<AdSize> adSizes;

    /**
     * @return The minimum window size for a {@link GoogleDfpAdModule}.
     */
    public WindowSize getMinimumWindowSize() {
        return minimumWindowSize;
    }

    /**
     * @param minimumWindowSize The minimum window size for a {@link GoogleDfpAdModule}.
     */
    public void setMinimumWindowSize(WindowSize minimumWindowSize) {
        this.minimumWindowSize = minimumWindowSize;
    }

    /**
     * @return List of supported {@link AdSize} for the configured {@link WindowSize}.
     */
    public List<AdSize> getAdSizes() {
        if (adSizes == null) {
            adSizes = new ArrayList<>();
        }
        return adSizes;
    }

    /**
     * @param adSizes The list of supported {@link AdSize}.
     */
    public void setAdSizes(List<AdSize> adSizes) {
        this.adSizes = adSizes;
    }

    /**
     * @return Overrides {@link Record#getLabel()} using {@link #minimumWindowSize} if defined.
     */
    @Override
    public String getLabel() {

        if (minimumWindowSize != null && (minimumWindowSize.getWidth() != null
            || minimumWindowSize.getHeight() != null)) {
            return Optional.ofNullable(minimumWindowSize.getWidth())
                .map(width -> width.toString())
                .orElse("")
                + "x"
                + Optional.ofNullable(minimumWindowSize.getHeight())
                .map(height -> height.toString())
                .orElse("");
        }

        return super.getLabel();
    }

    /**
     * @return Formats the {@link #minimumWindowSize} and {@link #adSizes} for {@link GoogleDfpAdModuleViewModel}.
     */
    public String toAdSizeMapString() {

        return "[" + minimumWindowSize.toDimensionString()
            + getAdSizes().stream()
            .map(AdSize::toDimensionString)
            .collect(Collectors.joining(", ", ", ", ""))
            + "]";
    }
}
