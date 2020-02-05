package brightspot.core.tool;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;

import com.psddev.cms.db.ToolUser;
import com.psddev.cms.tool.AuthenticationFilter;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PageContextFilter;

public class ToolUserUtils {

    public static ToolUser getCurrentToolUser() {
        HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();

        return request != null ? AuthenticationFilter.Static.getUser(request) : null;
    }

    public static ZoneId getUserZoneIdOrDefault(ToolUser user, ZoneId defaultZoneId) {

        ZoneId zoneId = null;

        if (user != null) {
            String timeZoneId = user.getTimeZone();

            if (!ObjectUtils.isBlank(timeZoneId)) {
                try {
                    zoneId = ZoneId.of(timeZoneId);
                } catch (DateTimeException e) {
                    // Ignore unparseable time zone IDs.
                }
            }
        }

        return zoneId == null
            ? defaultZoneId
            : zoneId;
    }

    public static Locale getUserLocale(ToolUser user) {

        return (user != null && user.getLocale() != null) ? user.getLocale() : Locale.getDefault();
    }
}
