package brightspot.core.requestextras.responseheader;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

/**
 * Editorially managed custom HTTP Response Header.
 */
@Recordable.Embedded
public final class ResponseHeader extends Record {

    @Required
    private String headerName;
    private String headerValue;

    public String getHeaderName() {
        return headerName;
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    @Override
    public String getLabel() {
        StringBuilder str = new StringBuilder();
        if (headerName != null) {
            str.append(headerName);
        }
        str.append(": ");
        if (headerValue != null) {
            str.append(headerValue);
        }
        return str.toString();
    }
}
