package bex.training.movie.query;

import brightspot.core.listmodule.DynamicQuerySort;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Release Date (Newest to Oldest)")
public class ReleaseDateDescendingDynamicQuerySort extends Record implements DynamicQuerySort {

    @Override
    public void updateQuery(Site site, Object o, Query<?> query) {
        query.where("bex.training.release.ReleasableData/releasable.releaseDate != missing")
                .sortDescending("bex.training.release.ReleasableData/releasable.releaseDate");
    }
}
