package brightspot.liveblog.wyntk;

import java.util.Optional;

import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Shared")
public class SharedWhatYouNeedToKnowOption extends WhatYouNeedToKnowOption {

    @Required
    private WhatYouNeedToKnow whatYouNeedToKnow;

    @Override
    public WhatYouNeedToKnow getWhatYouNeedToKnow() {

        return whatYouNeedToKnow;
    }

    public void setWhatYouNeedToKnow(WhatYouNeedToKnow whatYouNeedToKnow) {

        this.whatYouNeedToKnow = whatYouNeedToKnow;
    }

    // --- Recordable support --

    @Override
    public String getLabel() {

        return Optional.ofNullable(getWhatYouNeedToKnow())
            .map(WhatYouNeedToKnow::getLabel)
            .orElse(null);
    }
}
