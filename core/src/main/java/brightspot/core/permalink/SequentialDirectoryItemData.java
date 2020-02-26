package brightspot.core.permalink;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import brightspot.core.tool.CmsToolModification;
import com.psddev.cms.db.BulkUploadDraft;
import com.psddev.cms.db.Copyable;
import com.psddev.cms.db.Directory;
import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.ObjectIndex;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;

/**
 * Global modification that stores the auto-increment directory item index for the original object.
 */
public class SequentialDirectoryItemData extends Modification<Object> implements Copyable {

    private transient Integer directoryItemIndex;

    /**
     * @return the auto-increment directory item index .
     */
    public Integer getDirectoryItemIndex() {
        return directoryItemIndex;
    }

    private transient Set<Directory.Path> originalDuplicatePaths = null;

    private boolean shouldIncrementDirectoryItem() {
        return Optional.ofNullable(PermalinkRuleSettings.get(getOriginalObject()))
            .map(AbstractPermalinkRule::shouldAutoIncrement)
            .orElse(isGlobalAutoIncrementPermalinks());
    }

    public static boolean isGlobalAutoIncrementPermalinks() {
        return Query.from(CmsTool.class).first().as(CmsToolModification.class).isAutoIncrementPermalinks();
    }

    @Override
    public void onCopy(Object source) {
        directoryItemIndex = null;
    }

    @Override
    protected final boolean onDuplicate(ObjectIndex index) {

        if (shouldIncrementDirectoryItem()) {

            if (index != null) {
                String field = index.getField();

                if (Directory.PATHS_FIELD.equals(field)) {

                    Set<Directory.Path> duplicatePaths = as(Directory.Data.class).getPaths();
                    if (ObjectUtils.isBlank(duplicatePaths)) {
                        return false;
                    }
                    duplicatePaths = new HashSet<>(duplicatePaths);

                    if (directoryItemIndex == null) {
                        directoryItemIndex = 1;

                    } else {
                        directoryItemIndex++;
                    }

                    if (originalDuplicatePaths == null) {
                        originalDuplicatePaths = new HashSet<>(duplicatePaths);
                    }

                    Set<Directory.Path> originalPaths = new HashSet<>();
                    for (Directory.Path duplicatePath : duplicatePaths) {
                        Object originalObject = Directory.Static.findByPath(
                            duplicatePath.getSite(),
                            duplicatePath.getPath());
                        if (originalObject != null && !originalObject.equals(getOriginalObject())) {

                            Set<Directory.Path> originalObjectPaths = State.getInstance(originalObject)
                                .as(Directory.Data.class)
                                .getPaths();
                            if (originalObjectPaths != null) {
                                originalPaths.addAll(originalObjectPaths);
                            }
                        }
                    }

                    boolean incrementedDirectoryItems = false;

                    List<Directory.Path> newPaths = new ArrayList<>();

                    for (Directory.Path duplicatePath : duplicatePaths) {

                        // for bulk upload, originalPaths is empty when it should contain items from the upload batch,
                        // (batch is saved in one commit and the items aren't found in Directory#findByPath)
                        // so always increment in that case
                        if (originalPaths.contains(duplicatePath) || as(BulkUploadDraft.class).getUploadId() != null) {

                            String duplicatePathString = duplicatePath.getPath();

                            for (Directory.Path originalDuplicatePath : originalDuplicatePaths) {

                                String originalDuplicatePathString = originalDuplicatePath.getPath();

                                if (duplicatePathString.startsWith(originalDuplicatePathString)) {

                                    String pathDiff = duplicatePathString.substring(originalDuplicatePathString.length());

                                    boolean isDerivativePath =
                                        pathDiff.isEmpty() || ObjectUtils.to(Integer.class, pathDiff) != null;
                                    if (isDerivativePath) {
                                        newPaths.add(new Directory.Path(
                                            originalDuplicatePath.getSite(),
                                            originalDuplicatePath.getPath() + "-" + directoryItemIndex,
                                            originalDuplicatePath.getType()));
                                        incrementedDirectoryItems = true;
                                    }
                                }
                            }

                        } else {
                            newPaths.add(duplicatePath);
                        }
                    }

                    if (incrementedDirectoryItems) {

                        as(Directory.Data.class).clearPaths();
                        for (Directory.Path newPath : newPaths) {
                            as(Directory.Data.class).addPath(newPath.getSite(), newPath.getPath(), newPath.getType());
                        }

                        return true;
                    }
                }
            }

        }

        return false;
    }
}
