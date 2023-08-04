package brightspot.product;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.psddev.cms.tool.widget.AssociatedContentWidget;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;

public class ProductCollectionPageAssociatedContentWidget extends AssociatedContentWidget {

    @Override
    protected Class<? extends Recordable> getContainingClass() {
        return ProductCollection.class;
    }

    @Override
    protected Set<ObjectType> getAssociatedTypes() {
        return Stream.of(Product.class).map(ObjectType::getInstance).collect(Collectors.toSet());
    }

    @Override
    protected Class<? extends Recordable> getAssociationClass() {
        return Product.class;
    }

    @Override
    protected String getFullyQualifiedAssociationFieldName() {
        return Product.class.getName() + "/collections";
    }
}
