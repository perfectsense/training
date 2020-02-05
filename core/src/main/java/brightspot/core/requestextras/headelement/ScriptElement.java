package brightspot.core.requestextras.headelement;

import com.psddev.cms.view.ModelWrapper;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class ScriptElement extends HeadElement implements ModelWrapper {

    @Required
    private ScriptElementBody type;

    @Override
    public Object unwrap() {
        return type;
    }
}
