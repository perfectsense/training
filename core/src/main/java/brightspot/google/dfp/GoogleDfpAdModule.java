package brightspot.google.dfp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.core.ad.AdModule;
import brightspot.core.ad.AdSize;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

/**
 * Google DFP implementation of {@link AdModule}.
 */
@Recordable.DisplayName("DFP Ad")
public class GoogleDfpAdModule extends AdModule {

    private static final Pattern PATTERN = Pattern.compile("googletag.defineSlot\\('\\/[^'/*]*\\/([^']*)',\\s*([^\\']*)");

    private String name;

    @Required
    @ToolUi.Note("Ex: responsive_banner")
    private String slot;

    @CollectionMinimum(1)
    @Required
    private List<AdSize> sizes;

    private transient boolean importing = false;

    /**
     * @return The importing state, which will clears any validation errors on import {@link #onValidate()} ()}.
     */
    public boolean isImporting() {
        return importing;
    }

    /**
     * @param importing The importing state.
     */
    public void setImporting(boolean importing) {
        this.importing = importing;
    }

    private List<AdSizeMap> sizeMappings;

    /**
     * @return List of {@link AdSize} .
     */
    public List<AdSize> getSizes() {
        if (sizes == null) {
            sizes = new ArrayList<>();
        }
        return sizes;
    }

    /**
     * @param sizes The List of {@link AdSize}.
     */
    public void setSizes(List<AdSize> sizes) {
        this.sizes = sizes;
    }

    /**
     * @return The name of this {@link GoogleDfpAdModule}.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name of this {@link GoogleDfpAdModule}.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The slot name.
     */
    public String getSlot() {
        return slot;
    }

    /**
     * @param slot The slot name.
     */
    public void setSlot(String slot) {
        this.slot = slot;
    }

    /**
     * @return The size restrictions.
     */
    public List<AdSizeMap> getSizeMappings() {
        if (sizeMappings == null) {
            sizeMappings = new ArrayList<>();
        }
        return sizeMappings;
    }

    /**
     * @param sizeMappings The size mapping restrictions.
     */
    public void setSizeMappings(List<AdSizeMap> sizeMappings) {
        this.sizeMappings = sizeMappings;
    }

    /**
     * @return Overrides {@link com.psddev.dari.db.Record#getLabel()} using {@link #name} or {@link #slot} if defined.
     */
    @Override
    public String getLabel() {
        return ObjectUtils.firstNonBlank(name, slot, super.getLabel());
    }

    @Override
    public String getUniqueName() {
        return Stream.of(
            name,
            slot,
            getSizes().stream().map(AdSize::getUniqueName).collect(Collectors.joining(",")),
            getSizeMappings().stream().map(AdSizeMap::toAdSizeMapString).collect(Collectors.joining(",")))
            .map(str -> (str == null) ? "" : str)
            .collect(Collectors.joining("|"));
    }

    /**
     * Overrides {@link com.psddev.dari.db.Record#onValidate} and clears any errors when {@link #importing} is set to
     * true.
     */
    @Override
    protected void onValidate() {
        super.onValidate();
        if (importing) {
            this.getState().clearAllErrors();
        }
    }
}
