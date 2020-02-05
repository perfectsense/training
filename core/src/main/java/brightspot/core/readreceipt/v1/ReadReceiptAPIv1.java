package brightspot.core.readreceipt.v1;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import brightspot.core.readreceipt.ReadReceipt;
import brightspot.core.readreceipt.Readable;
import com.google.common.base.Preconditions;
import com.psddev.auth.AuthenticationEntity;
import com.psddev.auth.AuthenticationFilter;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.ClassFinder;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PageContextFilter;
import com.psddev.dari.util.RoutingFilter;
import com.psddev.dari.util.StringUtils;
import com.psddev.dari.util.UrlBuilder;
import com.psddev.dari.util.WebPageContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RoutingFilter.Path(application = "api/v1", value = ReadReceiptAPIv1.PATH)
public class ReadReceiptAPIv1 extends HttpServlet {

    public static final String PATH = "readReceipts";

    private static final String RESPONSE_CONTENT_TYPE = "application/json";

    public static final String PARAM_READABLE_ID = "readableId";
    public static final String PARAM_READ_DATE = "readDate";

    public static final String PARAM_ACTION_MARK_READ = "action-markRead";
    public static final String PARAM_ACTION_MARK_UNREAD = "action-markUnread";

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadReceiptAPIv1.class);

    /**
     * Returns the servlet URL as a String with specified parameters included in the query string.
     *
     * @param params key / value pairs to include in the returned servlet URL
     * @return the servlet URL to the {@link ReadReceiptAPIv1} servlet with specified parameters included in the query
     * string.
     */
    public static String getServletURL(String... params) {

        String path = StringUtils.removeEnd(RoutingFilter.Static.getApplicationPath("api/v1"), "/")
            + StringUtils.ensureStart(PATH, "/");

        HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();

        if (request == null) {
            return path;
        }

        UrlBuilder builder = new UrlBuilder(request).currentScheme().currentHost().path(path);

        for (int i = 0; i + 1 < params.length; i += 2) {

            builder.parameter(params[i], params[i + 1]);
        }

        return builder.toString();
    }

    /**
     * Delegates to {@link #doMarkRead(WebPageContext, UUID, AuthenticationEntity, Date)} and {@link
     * #doMarkUnread(WebPageContext, UUID, AuthenticationEntity)} based on existence of {@link #PARAM_ACTION_MARK_READ}
     * and {@link #PARAM_ACTION_MARK_UNREAD}, respectively.
     *
     * Delegated methods are responsible for writing their own response content.
     *
     * Returns 500 error if any Exception is caught from the delegated methods.
     *
     * Returns 404 if no valid action parameters is specified (one of {@link #PARAM_ACTION_MARK_READ} or {@link
     * #PARAM_ACTION_MARK_UNREAD}.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        WebPageContext page = new WebPageContext(getServletContext(), request, response);
        boolean isActionMarkRead = page.paramNamesList().contains(PARAM_ACTION_MARK_READ);
        boolean isActionMarkUnread = page.paramNamesList().contains(PARAM_ACTION_MARK_UNREAD);
        Date readDate = ObjectUtils.firstNonNull(page.param(Date.class, PARAM_READ_DATE), new Date());
        UUID readableId = page.param(UUID.class, PARAM_READABLE_ID);
        AuthenticationEntity authenticationEntity = AuthenticationFilter.getAuthenticatedEntity(request);
        response.setContentType(RESPONSE_CONTENT_TYPE);

        if (!isActionMarkRead && !isActionMarkUnread) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "API requires an action parameter.");
            return;
        }

        try {
            if (isActionMarkRead) {
                doMarkRead(page, readableId, authenticationEntity, readDate);
            }

            if (isActionMarkUnread) {
                doMarkUnread(page, readableId, authenticationEntity);
            }
        } catch (Exception e) {
            LOGGER.warn("Caught error!", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Private method to which {@link #doPost(HttpServletRequest, HttpServletResponse)} delegates when {@link
     * #PARAM_ACTION_MARK_UNREAD} is present in the request parameters.
     *
     * Marks a {@link Readable} as having NOT been read by the current {@link AuthenticationEntity}.
     *
     * Marking unread is accomplished by deleting a {@link ReadReceipt} for the specified {@link AuthenticationEntity}
     * and {@link Readable} if one exists.
     *
     * @param page
     * @param readableId the ID of the {@link Readable} for which the {@link ReadReceipt} should be deleted.
     * @param authenticationEntity the currently logged-in entity
     * @throws IOException
     */
    private void doMarkUnread(WebPageContext page, UUID readableId, AuthenticationEntity authenticationEntity)
        throws IOException {

        Preconditions.checkNotNull(authenticationEntity);
        Preconditions.checkNotNull(readableId);

        brightspot.core.readreceipt.Readable readable = Query.from(Readable.class).where("id = ?", readableId).first();
        Preconditions.checkNotNull(readable);

        UUID authenticationEntityId = authenticationEntity.getState().getId();

        ReadReceipt readReceipt = Query.from(ReadReceipt.class)
            .where(
                ReadReceipt.ENTITY_READABLE_FIELD + " = ?",
                authenticationEntityId.toString() + "_" + authenticationEntityId.toString())
            .first();
        if (readReceipt != null) {
            readReceipt.delete();
        }
        Map<String, Object> json = new HashMap<>();
        json.put("action", "markUnread");
        json.put("status", "success");

        for (Class<? extends MarkUnreadResponseEnhancer> enhancerClass : ClassFinder.findConcreteClasses(
            MarkUnreadResponseEnhancer.class)) {

            MarkUnreadResponseEnhancer enhancer;

            try {
                enhancer = enhancerClass.newInstance();
                try {
                    enhancer.enhanceResponse(json, authenticationEntity, page);
                } catch (Exception e) {
                    LOGGER.warn(enhancerClass.getName() + "#enhanceResponse threw exception", e);
                }
            } catch (Exception e) {
                LOGGER.warn("Failed to instantiate " + enhancerClass.getName(), e);
            }
        }

        page.write(ObjectUtils.toJson(json));
    }

    /**
     * Private method to which {@link #doPost(HttpServletRequest, HttpServletResponse)} delegates when {@link
     * #PARAM_ACTION_MARK_READ} is present in the request parameters.
     *
     * Marks a {@link Readable} as having been read by the current {@link AuthenticationEntity} at the {@link Date}
     * specified by {@link #PARAM_READ_DATE}.
     *
     * Marking read is accomplished by invoking {@link Readable#markRead(UUID, Date)}.
     *
     * @param page
     * @param readableId the ID of the {@link Readable} for which the {@link ReadReceipt} should be created or updated.
     * @param authenticationEntity the currently logged-in entity
     * @param readDate the {@link Date} for which the {@link ReadReceipt} should be marked.
     * @throws IOException
     */
    private void doMarkRead(
        WebPageContext page,
        UUID readableId,
        AuthenticationEntity authenticationEntity,
        Date readDate) throws IOException {

        Preconditions.checkNotNull(authenticationEntity);
        Preconditions.checkNotNull(readableId);

        Readable readable = Query.from(Readable.class).where("id = ?", readableId).first();
        Preconditions.checkNotNull(readable);

        UUID authenticationEntityId = authenticationEntity.getState().getId();

        readable.markRead(authenticationEntityId, readDate);

        // respond 200
        page.getResponse().setStatus(HttpServletResponse.SC_OK);

        Map<String, Object> json = new HashMap<>();
        json.put("action", "markRead");
        json.put("status", "success");

        for (Class<? extends MarkReadResponseEnhancer> enhancerClass : ClassFinder.findConcreteClasses(
            MarkReadResponseEnhancer.class)) {

            MarkReadResponseEnhancer enhancer;

            try {
                enhancer = enhancerClass.newInstance();
                try {
                    enhancer.enhanceResponse(json, authenticationEntity, page);
                } catch (Exception e) {
                    LOGGER.warn(enhancerClass.getName() + "#enhanceResponse threw exception", e);
                }
            } catch (Exception e) {
                LOGGER.warn("Failed to instantiate " + enhancerClass.getName(), e);
            }
        }

        page.write(ObjectUtils.toJson(json));
    }
}
