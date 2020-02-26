package brightspot.core.attachment;

import java.io.IOException;

import com.psddev.cms.db.Localization;
import com.psddev.cms.tool.ContentEditWidget;
import com.psddev.cms.tool.ContentEditWidgetPlacement;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.ObjectUtils;

import static com.psddev.dari.html.Nodes.*;

public class AttachmentUrlWidget extends ContentEditWidget {

    private String url;

    @Override
    public boolean shouldDisplay(ObjectType type) {
        return type.isInstantiableTo(Attachment.class);
    }

    @Override
    public boolean shouldDisplay(ToolPageContext page, Object content) {
        url = ObjectUtils.to(Attachment.class, content).getPermalink();
        return url != null;
    }

    @Override
    public double getPosition(ToolPageContext page, Object content, ContentEditWidgetPlacement placement) {
        return -20.0;
    }

    @Override
    public ContentEditWidgetPlacement getPlacement(ToolPageContext page, Object content) {
        return ContentEditWidgetPlacement.RIGHT;
    }

    @Override
    public String getHeading(ToolPageContext page, Object content) {
        return Localization.currentUserText(getClass(), "title");
    }

    @Override
    public void display(ToolPageContext page, Object content, ContentEditWidgetPlacement placement) throws IOException {
        if (url != null) {
            page.write(A
                .href(url)
                .target("_blank")
                .with(url)
            );
        }
    }
}
