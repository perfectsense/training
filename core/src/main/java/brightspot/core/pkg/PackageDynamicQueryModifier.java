package brightspot.core.pkg;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.core.classification.ClassificationDynamicQueryModifier;
import brightspot.core.listmodule.DynamicItemStream;
import brightspot.core.timedcontentitemstream.DynamicTimedContentItemStream;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;

@Recordable.FieldInternalNamePrefix("packages.")
@Modification.Classes({
    DynamicItemStream.class,
    DynamicTimedContentItemStream.class,
    PackageDynamicQueryModifiable.class })
public class PackageDynamicQueryModifier extends Modification<Object> implements
    ClassificationDynamicQueryModifier {

    @ToolUi.DropDown
    @ToolUi.Cluster(CLASSIFICATION_CLUSTER)
    private Set<PackageOrCurrentPackage> packages;

    public Set<PackageOrCurrentPackage> getPackages() {
        if (packages == null) {
            packages = new HashSet<>();
        }
        return packages;
    }

    public void setPackages(Set<PackageOrCurrentPackage> packages) {
        this.packages = packages;
    }

    @Override
    public void updateQuery(Site site, Object mainObject, Query<?> query) {

        this.updateQueryWithClassificationQuery(
            query,
            mainObject,
            getPackages(),
            PackageOrCurrentPackage.class,
            Package.class);
    }

    @Override
    public String createLabel() {
        return getPackages().stream()
            .map(pkg -> pkg.getState().getLabel())
            .collect(Collectors.joining(", "));
    }
}
