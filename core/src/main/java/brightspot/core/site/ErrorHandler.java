package brightspot.core.site;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psddev.dari.db.Record;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
abstract class ErrorHandler extends Record {

    public abstract boolean handleStatusCode(HttpServletRequest request, HttpServletResponse response, int statusCode);

    public abstract boolean handleException(
        HttpServletRequest request,
        HttpServletResponse response,
        Throwable exception);
}
