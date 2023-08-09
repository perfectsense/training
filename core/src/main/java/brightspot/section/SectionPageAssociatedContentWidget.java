package brightspot.section;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.psddev.cms.tool.widget.AssociatedContentWidget;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ClassFinder;

public class SectionPageAssociatedContentWidget extends AssociatedContentWidget {

    @Override
    protected Class<? extends Recordable> getContainingClass() {
        return Section.class;
    }

    @Override
    protected Set<ObjectType> getAssociatedTypes() {
        return Optional.of(HasSection.class)
            .map(ClassFinder::findConcreteClasses)
            .map(targetClasses -> targetClasses
                .stream()
                .map(ObjectType::getInstance)
                .collect(Collectors.toSet()))
            .orElseGet(HashSet::new);
    }

    @Override
    protected Class<? extends Recordable> getAssociationClass() {
        return HasSection.class;
    }

    @Override
    protected String getFullyQualifiedAssociationFieldName() {
        return HasSectionData.class.getName() + "/" + HasSectionData.ANCESTORS_FIELD;
    }
}
