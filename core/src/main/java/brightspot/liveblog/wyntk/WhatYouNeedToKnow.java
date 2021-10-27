package brightspot.liveblog.wyntk;

import brightspot.rte.LargeRichTextToolbar;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;

@ToolUi.Permissionable
@Content.Searchable
@ToolUi.ExcludeFromGlobalSearch
public class WhatYouNeedToKnow extends Record {

    @Required
    @IgnoredIfEmbedded
    private String internalName;

    @ToolUi.Unlabeled
    @ToolUi.RichText(toolbar = LargeRichTextToolbar.class, lines = 10, inline = false)
    private String whatYouNeedToKnow;

    public String getInternalName() {

        return internalName;
    }

    public void setInternalName(String internalName) {

        this.internalName = internalName;
    }

    public String getWhatYouNeedToKnow() {

        return whatYouNeedToKnow;
    }

    public void setWhatYouNeedToKnow(String whatYouNeedToKnow) {

        this.whatYouNeedToKnow = whatYouNeedToKnow;
    }

    // --- Recordable support ---

    @Override
    public String getLabel() {

        return getInternalName();
    }
}
