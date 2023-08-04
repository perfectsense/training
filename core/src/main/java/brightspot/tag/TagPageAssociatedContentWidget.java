package brightspot.tag;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.psddev.cms.tool.widget.AssociatedContentWidget;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ClassFinder;

public class TagPageAssociatedContentWidget extends AssociatedContentWidget {

    @Override
    protected Class<? extends Recordable> getContainingClass() {
        return Tag.class;
    }

    @Override
    protected Set<ObjectType> getAssociatedTypes() {
        return Optional.of(HasTags.class)
            .map(ClassFinder::findConcreteClasses)
            .map(targetClasses -> targetClasses
                .stream()
                .map(ObjectType::getInstance)
                .collect(Collectors.toSet()))
            .orElseGet(HashSet::new);
    }

    @Override
    protected Class<? extends Recordable> getAssociationClass() {
        return HasTags.class;
    }

    @Override
    protected String getFullyQualifiedAssociationFieldName() {
        return HasTagsData.class.getName() + "/" + HasTagsData.TAGS_AND_ANCESTORS_FIELD;
    }
}
