package brightspot.core.cascading;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.psddev.cms.db.ImageTag;
import com.psddev.cms.db.Localization;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.SiteSettings;
import com.psddev.cms.tool.CmsTool;
import com.psddev.cms.tool.ToolPageContext;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PageContextFilter;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;

/**
 * Convenience methods to resolve {@link Cascading} page elements and render Tool NoteHtmls and Placeholders.
 */
public class CascadingPageElementsModification extends Modification<Object> {

    public static final String INTERNAL_NAME = "brightspot.core.cascading.CascadingPageElementsModification";

    /**
     * For use in @ToolUi.NoteHtml.
     *
     * Example:
     *
     * <pre>{@code
     *     @ToolUi.NoteHtml("<span data-dynamic-html=\"${content.asCascadingPageElementsModification().createNoteHtml('page.header')}\"></span>")
     * }</pre>
     */
    public String createNoteHtml(String fieldName) {

        Recordable container = getCascadingPageElementsContainer();
        if (container instanceof CmsTool) {
            return null;
        }

        Object item = getState().getByPath(fieldName);
        if (item instanceof Cascading<?>) {
            item = ((Cascading<?>) item).get();
        }

        Site site = null;
        HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();
        HttpServletResponse response = PageContextFilter.Static.getResponseOrNull();
        ServletContext servletContext = PageContextFilter.Static.getServletContext();

        ToolPageContext context = null;
        if (request != null && response != null && servletContext != null) {
            //noinspection IOResourceOpenedButNotSafelyClosed
            context = new ToolPageContext(servletContext, request, response);
            site = context.getSite();
        }

        if (container instanceof Site) {
            site = (Site) container;
        }

        if (site == null) {
            site = as(Site.ObjectModification.class).getOwner();
        }

        CascadingStrategy strategy = CascadingStrategy.getInstance(getOriginalObject(), site);

        //noinspection unchecked
        CascadingResult<?> result = strategy.resolveParent(
            container instanceof Site ? null : getOriginalObject(),
            site,
            csp -> Optional.ofNullable(State.getInstance(csp).getByPath(fieldName))
                .filter(Cascading.class::isInstance)
                .map(Cascading.class::cast)
                .orElse(null));

        Optional<Object> providerOriginalObject = Optional.ofNullable(result)
            .flatMap(CascadingResult::getProviderOriginalObject);

        if (providerOriginalObject.isPresent()) {
            Object provider = providerOriginalObject.get();
            UUID providerId = State.getInstance(provider).getId();
            String providerLabel = State.getInstance(provider).getLabel();
            String cmsUrl = null;
            if (context != null && Query.fromAll().where("_id = ?", providerId).hasMoreThan(0)) {
                cmsUrl = context.cmsUrl("content/edit.jsp", "id", providerId);
                if (provider instanceof Site || provider instanceof CmsTool) {
                    cmsUrl = context.cmsUrl("admin/sites.jsp", "id", providerId);
                    if (provider instanceof CmsTool) {
                        providerLabel = "Global";
                    }
                }
            }

            StringBuilder str = new StringBuilder();
            Object resultValue = Optional.ofNullable(result.getResult())
                .map(Cascading::get)
                .orElse(null);
            String defaultValuePreviewHtml = getPreviewHtml(resultValue, context);
            if (!StringUtils.isBlank(defaultValuePreviewHtml)) {
                str.append(defaultValuePreviewHtml);
                str.append("<br />");
            }

            // Falling back to (Object Name) as set in (my Site).

            String previewLabel = getPreviewLabel(resultValue, context);
            if (result.getResult() == null) {
                str.append("No default.");

            } else {
                str.append("Falling back to ");
                str.append(previewLabel);
                str.append(" as set in ");
                if (!ObjectUtils.isBlank(cmsUrl)) {
                    str.append("<a href=\"");
                    str.append(cmsUrl);
                    str.append("\">");
                    str.append(providerLabel);
                    str.append("</a>");
                } else {
                    str.append(providerLabel);
                }
                String appendNote = result.getAppendNote();
                if (!StringUtils.isBlank(appendNote)) {
                    str.append(appendNote);
                }
                str.append('.');
            }
            return str.toString();

        } else {
            return "No default.";
        }
    }

    /**
     * For use in @ToolUi.Placeholder.
     *
     * Example:
     *
     * <pre>{@code
     *     @ToolUi.Placeholder(dynamicText = "${content.asCascadingPageElementsModification().createPlaceholder()}")
     * }</pre>
     */
    public String createPlaceholder() {
        if (getCascadingPageElementsContainer() instanceof CmsTool) {
            return Localization.currentUserText(null, "option.none", "None");
        }
        return Localization.currentUserText(null, "option.inherit", "Inherit");
    }

    /**
     * Return the value, given a class that is a modification of CascadingSettingsProvider and a method that returns the
     * Cascading field value.
     *
     * For example, in {@link brightspot.core.page.CascadingPageData#getNavigation(Site)}, the implementation is: {@code
     * public Header getHeader(Site site) { return as(CascadingPageElementsData.class) .get(site, PageData.class,
     * PageData::getCascadingHeader); }
     *
     * @param site The current site
     * @param modificationClass A {@code Modification&lt;CascadingSettingsProvider&gt;} class
     * @param getter A method on that class
     * @return The value
     */
    public <T, C extends Modification> T get(Site site, Class<C> modificationClass, Function<C, Cascading<T>> getter) {
        return get(
            site,
            cpe -> Optional.ofNullable(State.getInstance(cpe).as(modificationClass))
                .map(getter)
                .orElse(null));
    }

    /**
     * Return the value, given a function that retrieves the value from an object
     *
     * @param site The current site
     * @param getter A method to be called on every CascadingPageElements found by the configured {@link
     * CascadingStrategy}
     * @return The value
     */
    public <T> T get(Site site, Function<Object, Cascading<T>> getter) {
        CascadingStrategy strategy = CascadingStrategy.getInstance(getOriginalObject(), site);
        return strategy.get(getOriginalObject(), site, getter);
    }

    /**
     * Return the value given a field name.
     *
     * @param site The current site
     * @param fieldName The desired fieldName in the {@link com.psddev.dari.db.State}
     */
    public Object getByPath(Site site, String fieldName) {
        @SuppressWarnings("unchecked")
        Object item = get(site, cpe -> Optional.ofNullable(State.getInstance(cpe).getByPath(fieldName))
            .filter(Cascading.class::isInstance)
            .map(Cascading.class::cast)
            .orElse(null));
        return item;
    }

    private Recordable getCascadingPageElementsContainer() {
        Object originalObject = getOriginalObject();
        return originalObject instanceof CascadingPageElements
            ? ((CascadingPageElements) originalObject).getCascadingPageElementsContainer()
            : originalObject instanceof SiteSettings
                ? (SiteSettings) originalObject
                : null;
    }

    private static String getPreviewLabel(Object result, ToolPageContext context) {
        if (result instanceof Collection<?>) {
            Collection<?> resultCollection = (Collection<?>) result;

            if (resultCollection.isEmpty()) {
                return Localization.currentUserText(null, "option.none", "None");
            }

            return resultCollection.stream()
                .map(obj -> findPreviewLabel(obj, context))
                .collect(Collectors.joining(", "));
        } else {
            return findPreviewLabel(result, context);
        }
    }

    private static String findPreviewLabel(Object obj, ToolPageContext context) {
        if (obj instanceof Recordable) {
            State resultState = State.getInstance(obj);
            String label = ToolPageContext.Static.getObjectLabelOrDefault(
                resultState,
                ToolPageContext.DEFAULT_OBJECT_LABEL);
            if (context != null && Query.fromAll().where("_id = ?", resultState.getId()).hasMoreThan(0)) {
                label = "<a href=\"" + context.cmsUrl("content/edit.jsp", "id", resultState.getId()) + "\">" + label
                    + "</a>";
            }
            return resultState.getType().getDisplayName() + ": " + label;
        }
        if (obj != null) {
            String resultString = obj.toString();
            if (resultString.length() < 25) {
                return resultString;
            }
            return resultString.substring(0, 22) + "...";
        }
        return Localization.currentUserText(null, "option.none", "None");
    }

    private static String getPreviewHtml(Object result, ToolPageContext context) {
        if (result instanceof Recordable) {
            State resultState = State.getInstance(result);

            String previewField = resultState.getType().getPreviewField();
            if (!StringUtils.isBlank(previewField)) {
                Object previewValue = resultState.getByPath(previewField);
                if (previewValue instanceof StorageItem) {
                    StringBuilder str = new StringBuilder();
                    str.append("<img src=\"");
                    str.append(new ImageTag.Builder((StorageItem) previewValue).setHeight(100).toUrl());
                    str.append("\" style=\"width: auto; height: 100px; border:solid 1px #cdcdcd; padding: 3px;\"/>");
                    return str.toString();
                }
            }
        }
        return null;
    }
}
