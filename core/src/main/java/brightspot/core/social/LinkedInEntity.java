package brightspot.core.social;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import org.apache.commons.lang3.StringUtils;

/**
 * The social field for LinkedIn in {@link SocialEntityData}. Used to produce a LinkedIn URL.
 */
@Recordable.Embedded
public abstract class LinkedInEntity extends Record {

    @ToolUi.Unlabeled
    private String linkedInUsername;

    public String getLinkedInUsername() {
        return linkedInUsername;
    }

    public void setLinkedInUsername(String linkedInUsername) {
        this.linkedInUsername = linkedInUsername;
    }

    public abstract String getBaseUrl();

    /**
     * @return the LinkedIn URL.
     */
    public String getUrl() {
        return !StringUtils.isBlank(linkedInUsername)
            ? getBaseUrl() + linkedInUsername
            : null;
    }
}
