package brightspot.core.promo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.module.Module;
import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.ObjectField;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.CompactMap;

@Recordable.DisplayName("Shared")
@Recordable.Embedded
@Recordable.TypePostProcessorClasses(SharedPromoOptionPostProcessor.class)
public class SharedPromoOption extends Record implements PromoOption, ModelWrapper {

    @Required
    private Module module;

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public Object unwrap() {
        return getModule();
    }

    @Override
    public Promo getPromoOptionPromo() {
        return Optional.ofNullable(getModule())
            .map(Module::getType)
            .filter(Promo.class::isInstance)
            .map(Promo.class::cast)
            .orElse(null);
    }

    // Temporary to work around a bug in Dari.
    @Override
    protected void onValidate() {
        State state = getState();
        Map<ObjectField, List<String>> errorsByField = new CompactMap<>();

        for (ObjectField field : state.getErrorFields()) {
            if ("module".equals(field.getInternalName())) {
                errorsByField.put(field, state.getErrors(field)
                    .stream()
                    .filter(error -> error == null || !error.contains("Must match"))
                    .collect(Collectors.toList()));

            } else {
                errorsByField.put(field, state.getErrors(field));
            }
        }

        state.clearAllErrors();
        errorsByField.forEach((field, errors) -> errors.forEach(error -> state.addError(field, error)));
    }
}
