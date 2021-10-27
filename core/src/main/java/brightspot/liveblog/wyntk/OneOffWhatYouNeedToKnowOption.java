package brightspot.liveblog.wyntk;

import java.util.Optional;

import brightspot.util.RichTextUtils;
import brightspot.util.Truncate;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("One-Off")
public class OneOffWhatYouNeedToKnowOption extends WhatYouNeedToKnowOption {

    @Embedded
    @Required
    @ToolUi.Unlabeled
    private WhatYouNeedToKnow whatYouNeedToKnow;

    @Override
    public WhatYouNeedToKnow getWhatYouNeedToKnow() {

        return whatYouNeedToKnow;
    }

    public void setWhatYouNeedToKnow(WhatYouNeedToKnow whatYouNeedToKnow) {

        this.whatYouNeedToKnow = whatYouNeedToKnow;
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {

        return Optional.ofNullable(getWhatYouNeedToKnowText())
            .map(RichTextUtils::stripRichTextElements)
            .map(RichTextUtils::richTextToPlainText)
            .map(text -> Truncate.truncate(text, 100, true))
            .orElse(null);
    }
}
