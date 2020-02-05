package brightspot.core.tool;

import com.google.common.base.Preconditions;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Workflow;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;

public class ContentUtils {

    public static boolean isFirstPublish(Object object) {

        Preconditions.checkNotNull(object);

        State state = State.getInstance(object);

        Object existingObject = Query.fromAll().where("id = ?", state.getId()).noCache().first();

        State existingState = State.getInstance(existingObject);

        // this Object is already published if:
        //  - it already exists in the database
        //  - AND the database object is not in draft
        //  - AND the database object is not trashed
        //  - AND the database object is not in workflow
        boolean isAlreadyPublished = existingObject != null
            && !existingState.as(Content.ObjectModification.class).isDraft()
            && !existingState.as(Content.ObjectModification.class).isTrash()
            && existingState.as(Workflow.Data.class).getCurrentState() == null;

        // this Object is about to be published for the first time if:
        //  - this Object is not already published (above)
        //  - AND it is not in draft
        //  - AND it is not trashed
        //  - AND it is not in workflow
        return !isAlreadyPublished
            && !state.as(Content.ObjectModification.class).isDraft()
            && !state.as(Content.ObjectModification.class).isTrash()
            && state.as(Workflow.Data.class).getCurrentState() == null;
    }

    public static boolean isPublish(Object object) {

        Preconditions.checkNotNull(object);

        State state = State.getInstance(object);

        return !state.as(Content.ObjectModification.class).isDraft()
            && !state.as(Content.ObjectModification.class).isTrash()
            && state.as(Workflow.Data.class).getCurrentState() == null;
    }
}
