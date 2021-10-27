package brightspot.liveblog.wyntk;

import java.util.Optional;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class WhatYouNeedToKnowOption extends Record {

    public abstract WhatYouNeedToKnow getWhatYouNeedToKnow();

    public String getWhatYouNeedToKnowText() {

        return Optional.ofNullable(getWhatYouNeedToKnow())
            .map(WhatYouNeedToKnow::getWhatYouNeedToKnow)
            .orElse(null);
    }
}
