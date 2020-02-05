package brightspot.core.requestextras.headelement;

import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("Link")
public class ExternalScriptElementBody extends ScriptElementBody {

    @DisplayName("Script URL")
    private String src;

    private boolean async;

    private boolean defer;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isDefer() {
        return defer;
    }

    public void setDefer(boolean defer) {
        this.defer = defer;
    }
}
