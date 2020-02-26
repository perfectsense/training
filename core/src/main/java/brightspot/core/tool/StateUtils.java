package brightspot.core.tool;

import java.util.function.Function;

import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;

/**
 * Lazy load state values if the state is {@link State#isResolveToReferenceOnly()}.
 *
 * Usage:
 *
 * <pre>
 * {@code
 *     Article article = Query.from(Article.class)
 *             .resolveToReferenceOnly()
 *             .first();
 *
 *     return StateUtils
 *             .resolveAndGet(article, a -> a.getPromotableImage());
 * }
 * </pre>
 *
 * This should be used in all indexed methods if referenced objects are accessed.
 */
public final class StateUtils {

    private StateUtils() {
    }

    public static <S, T> T resolveAndGet(S record, Function<S, T> getter) {
        boolean addListener = State.getInstance(record).isResolveToReferenceOnly();
        UnresolvedStateLazyLoader listener = null;

        if (addListener) {
            listener = new UnresolvedStateLazyLoader();
            State.Static.addListener(listener);
        }

        try {
            return getter.apply(record);

        } finally {
            if (addListener) {
                State.Static.removeListener(listener);
            }
        }
    }

    public static <T> T resolve(T object) {

        if (object == null) {
            return null;
        }

        State state = State.getInstance(object);

        if (state.isReferenceOnly()) {
            return (T) Query.fromAll().where("_id = ?", state.getId()).noCache().first();
        } else {
            return object;
        }
    }
}
