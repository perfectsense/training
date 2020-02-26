package brightspot.core.link;

import java.util.ArrayList;
import java.util.List;

import brightspot.core.tool.DefaultImplementationSupplier;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public abstract class Link extends Record {

    public static final String ADVANCED_TAB = "Advanced";

    @ToolUi.Placeholder("Same Window/Tab")
    @ToolUi.Tab(ADVANCED_TAB)
    private Target target;

    @ToolUi.Tab(ADVANCED_TAB)
    private List<Attribute> attributes;

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public List<Attribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<>();
        }
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public abstract String getLinkUrl(Site site);

    public String getLinkTextFallback() {
        return null;
    }

    public static Link createDefault() {
        return DefaultImplementationSupplier.createDefault(Link.class, InternalLink.class);
    }
}
