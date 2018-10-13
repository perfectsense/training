package bex.training.movie.query;

import brightspot.core.listmodule.DynamicQuerySort;
import com.psddev.cms.db.Site;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Release Date (Oldest to Newest)")
public class ReleaseDateAscendingDynamicQuerySort extends Record implements DynamicQuerySort {

    @Override
    public void updateQuery(Site site, Object mainObject, Query<?> query) {
        query.where("bex.training.release.ReleasableData/releasable.releaseDate != missing")
                .sortAscending("bex.training.release.ReleasableData/releasable.releaseDate");
    }
}
