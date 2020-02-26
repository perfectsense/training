package brightspot.core.tool;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.cms.tool.ToolPageHead;
import com.psddev.dari.db.Database;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.util.ObjectUtils;

public class AddToTopToolPageHead implements ToolPageHead {

    private static final Supplier<Set<ObjectType>> ADD_TO_TOP_TYPES = Suppliers.memoizeWithExpiration(
        () -> Database.Static.getDefault().getEnvironment().getTypes().stream()
            .filter(type -> type.getFields()
                .stream()
                .anyMatch(field -> field.as(AddToTop.FieldData.class).isAddToTop()))
            .collect(Collectors.toSet()),
        1L, TimeUnit.MINUTES
    );

    public void writeHtml(ToolPageContext page) throws IOException {

        page.writeStart("script");
        {
            page.writeRaw("window.ADD_TO_TOP_FIELDS = "
                    + ObjectUtils.toJson(
                ADD_TO_TOP_TYPES.get().stream()
                    .map(type -> type.getFields().stream()
                        .filter(field -> field.as(AddToTop.FieldData.class).isAddToTop())
                        .map(field -> ImmutableMap.of(
                            "class",
                            type.getInternalName(),
                            "field",
                            field.getInternalName()))
                        .collect(Collectors.toList())
                    )
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList())
                )
                    + ";"
            );
        }
        page.writeEnd();
    }
}
