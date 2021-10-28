package training;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUser;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.web.WebRequestExtension;

public class ExportRefsWebExtension extends WebRequestExtension {

    private final Set<UUID> refs = new HashSet<>();
    private final Queue<UUID> queue = new ArrayDeque<>();

    public Queue<UUID> getRefs() {
        return queue;
    }

    public void addRef(Recordable recordable) {
        Objects.requireNonNull(recordable);
        if (recordable instanceof Site || recordable instanceof ToolUser) {
            return;
        }

        UUID id = recordable.getState().getId();
        if (refs.add(id)) {
            queue.add(id);
        }
    }
}
