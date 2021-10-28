package training;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.link.Link;
import brightspot.core.link.Target;
import com.google.common.collect.ImmutableMap;
import com.psddev.cms.view.JsonView;
import com.psddev.cms.view.ViewInterface;
import com.psddev.cms.view.ViewKey;

@JsonView
@ViewInterface
public abstract class LinkViewModel<T extends Link> extends ExportViewModel<T> {

    @ViewKey("target")
    public String getTarget() {
        return Optional.ofNullable(model.getTarget())
            .map(Target::name)
            .orElse(null);
    }

    @ViewKey("attributes")
    public List<Map<String, String>> getAttributes() {
        return model.getAttributes()
            .stream()
            .map(a -> ImmutableMap.of(
                a.getName(),
                a.getValue()))
            .collect(Collectors.toList());
    }
}
