package brightspot.google.adsense;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import brightspot.core.ad.AdModule;
import brightspot.core.ad.AdSize;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;

@Recordable.DisplayName("Ad Sense Ad")
public class GoogleAdSenseAdModule extends AdModule {

    private String name;

    @Required
    @ToolUi.Note("Ex: 8320618289")
    private String slot;

    private AdSize adSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AdSize getAdSize() {
        return adSize;
    }

    public void setAdSize(AdSize adSize) {
        this.adSize = adSize;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    @Override
    public String getLabel() {
        return ObjectUtils.firstNonBlank(name, slot, super.getLabel());
    }

    @Override
    public String getUniqueName() {
        return Stream.of(
            name,
            slot,
            Optional.ofNullable(getAdSize()).map(AdSize::getUniqueName).orElse(null))
            .map(str -> (str == null) ? "" : str)
            .collect(Collectors.joining("|"));
    }
}
