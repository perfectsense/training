package brightspot.core.asset;

import java.util.stream.Collectors;

import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.TypeDefinition;

class AssetMimeTypesProcessor implements ObjectType.AnnotationProcessor<AssetMimeTypes> {

    @Override
    public void process(ObjectType type, AssetMimeTypes annotation) {
        if (type.getGroups().contains(AbstractAsset.class.getName())) {
            type.getField("file").setMimeTypes(
                TypeDefinition.getInstance(annotation.value()).newInstance().get().stream()
                    .map(mimeType -> "+" + mimeType)
                    .collect(Collectors.joining(" ")));
        }
    }
}
