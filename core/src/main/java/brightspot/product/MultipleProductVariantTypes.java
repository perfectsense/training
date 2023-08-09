package brightspot.product;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Options Available")
@ToolUi.FieldDisplayOrder({
    "getOptions",
    "variants"
})
public class MultipleProductVariantTypes extends ProductVariantType {

    private List<ProductVariant> variants;

    @Ignored(value = false)
    @Indexed
    public List<ProductOption> getOptions() {
        return getVariants()
            .stream()
            .map(ProductVariant::getOptions)
            .flatMap(Collection::stream)
            .map(ProductVariantOption::getName)
            .collect(Collectors.toSet())
            .stream()
            .map(name -> {
                ProductOption option = new ProductOption();
                option.setName(name);
                option.getState().setId(UUID.nameUUIDFromBytes((this.getId() + "/" + option.getName()).getBytes(
                    StandardCharsets.UTF_8)));
                return option;
            })
            .map(productOption -> {
                getVariants()
                    .stream()
                    .map(ProductVariant::getOptions)
                    .flatMap(Collection::stream)
                    .filter(variantOption -> variantOption.getName().equals(productOption.getName()))
                    .forEach(variantOption -> productOption.getValues().add(variantOption.getValue()));
                return productOption;
            })
            .collect(Collectors.toList());
    }

    public List<ProductVariant> getVariants() {
        if (variants == null) {
            variants = new ArrayList<>();
        }
        return variants;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }

    @Override
    public List<ProductVariant> toVariants() {
        return getVariants();
    }

    @Override
    public void fromVariants(List<ProductVariant> fromVariants) {
        setVariants(fromVariants);
    }
}
