package brightspot.core.timed;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.State;
import com.psddev.dari.util.HtmlWriter;
import com.psddev.dari.util.PageContextFilter;

/**
 * Note renderer for displaying the "effective" list of {@linkplain TimedCompanion} objects when using a {@linkplain
 * CascadingTimedCompanionList}.
 */
public class CascadingTimedCompanionListNoteRenderer implements ToolUi.NoteRenderer {

    private TimedContent timedContent;
    private CascadingTimedCompanionList timedCompanionList;

    public CascadingTimedCompanionListNoteRenderer() {
    }

    public CascadingTimedCompanionListNoteRenderer(
        TimedContentProvider timedContentProvider,
        CascadingTimedCompanionList timedCompanionList) {
        this.timedContent = timedContentProvider.getTimedContent();
        this.timedCompanionList = timedCompanionList;
    }

    public CascadingTimedCompanionListNoteRenderer(
        TimedContent timedContent,
        CascadingTimedCompanionList timedCompanionList) {
        this.timedContent = timedContent;
        this.timedCompanionList = timedCompanionList;
    }

    private ToolPageContext getToolPageContext() {

        HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();
        HttpServletResponse response = PageContextFilter.Static.getResponseOrNull();

        if (request == null || response == null) {
            return null;
        }

        return new ToolPageContext(request.getServletContext(), request, response);
    }

    private TimedContent getTimedContent(Object object, ObjectField field, ToolPageContext page) {

        if (timedContent != null) {
            return timedContent;
        }

        if (object instanceof TimedContent) {
            return (TimedContent) object;
        }

        if (object instanceof TimedContentProvider) {
            return ((TimedContentProvider) object).getTimedContent();
        }

        return null;
    }

    private CascadingTimedCompanionList getTimedCompanionList(Object object, ObjectField field, ToolPageContext page) {

        if (timedCompanionList != null) {
            return timedCompanionList;
        }

        State state = State.getInstance(object);

        if (state == null || field == null) {
            return null;
        }

        Object fieldValue = state.get(field.getInternalName());

        if (!(fieldValue instanceof CascadingTimedCompanionList)) {
            return null;
        }

        return (CascadingTimedCompanionList) fieldValue;
    }

    @Override
    public String render(Object object) {
        return render(object, null, getToolPageContext());
    }

    public String render(Object object, ObjectField field, ToolPageContext page) {

        TimedContent timedContent = getTimedContent(object, field, page);
        CascadingTimedCompanionList timedCompanionList = getTimedCompanionList(object, field, page);

        Set<UUID> existingCompanionIds = Optional.ofNullable(timedContent)
            .map(tc -> tc.asTimedContentData().getTimedCompanions())
            .orElse(Collections.emptyList())
            .stream()
            .map(TimedCompanion::getId)
            .collect(Collectors.toSet());

        List<TimedCompanion> timedCompanions = CascadingTimedCompanionList.getItemsMerged(
            () -> timedContent,
            timedCompanionList);

        if (timedCompanions.isEmpty()) {
            return null;
        }

        StringWriter html = new StringWriter();
        HtmlWriter writer = new HtmlWriter(html);

        try {
            writer.writeStart("ol");
            {
                for (TimedCompanion timedCompanion : timedCompanions) {
                    writer.writeStart(
                        "li",
                        "style",
                        !existingCompanionIds.contains(timedCompanion.getId()) ? "color:#000;" : null);
                    {
                        writer.writeHtml(page.getObjectLabel(timedCompanion));

                        Content companionContent = timedCompanion.getCompanionContent();

                        if (companionContent != null) {
                            writer.writeStart("a",
                                "class", "objectId-edit",
                                "target", getClass().getName(),
                                "href", page.objectUrl("/content/edit.jsp", timedCompanion.getCompanionContent()));
                            {
                                // TODO: Localize
                                writer.writeHtml("Edit");
                            }
                            writer.writeEnd();
                        }
                    }
                    writer.writeEnd();
                }
            }
            writer.writeEnd();

        } catch (IOException e) {
            throw new IllegalStateException();
        }

        return html.toString();
    }
}
