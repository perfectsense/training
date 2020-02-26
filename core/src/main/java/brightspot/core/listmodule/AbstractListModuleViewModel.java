package brightspot.core.listmodule;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import brightspot.core.page.HttpIdPrefixedParameters;
import brightspot.core.page.HttpIdPrefixedParametersProcessor;
import brightspot.core.page.HttpQueryString;
import brightspot.core.page.IdParameterMap;
import brightspot.core.promo.InternalPromoItem;
import brightspot.core.promo.Promo;
import brightspot.core.promo.Promotable;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.ViewModel;
import com.psddev.cms.view.ViewResponse;
import com.psddev.cms.view.servlet.CurrentSite;
import com.psddev.cms.view.servlet.MainObject;
import com.psddev.dari.db.ObjectType;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.State;
import com.psddev.dari.util.StringUtils;
import com.psddev.styleguide.core.link.LinkView;

/**
 * Provide for basic pagination and rendering of {@link AbstractListModule}s via an ID-prefixed "page" query parameter.
 * In this way, multiple AbstractListModules can appear on the same page without their page parameters colliding.
 */
public abstract class AbstractListModuleViewModel<M extends AbstractListModule> extends ViewModel<M> {

    protected static final String PAGE_PARAMETER = "page";

    protected static final String PAGINATION_ID_EXTRA = "listModule.paginationId";

    @MainObject
    protected Object mainObject;

    @CurrentSite
    protected Site site;

    @HttpIdPrefixedParameters(PAGE_PARAMETER)
    protected IdParameterMap pageParam;

    @HttpQueryString
    protected String queryString;

    protected Integer page;

    protected long offset;

    protected int limit;

    protected String itemStreamType;

    protected boolean hasNextPage;

    /**
     * Set the ID on a transient ListModule (one that was created on the fly) for the purpose of pagination.
     */
    public static void setPaginationId(AbstractListModule listModule, UUID transientId) {
        if (listModule == null) {
            return;
        }

        listModule.getState().getExtras().put(PAGINATION_ID_EXTRA, transientId);
    }

    /**
     * Return the ID of a list module as set by {@link #setPaginationId}, falling back to the ListModule's actual record
     * ID.
     *
     * This is the the ID that should prefix the pagination parameter.
     */
    public static UUID getPaginationId(AbstractListModule listModule) {
        if (listModule == null) {
            return null;
        }

        Object paginationId = State.getInstance(listModule)
            .getExtras()
            .get(PAGINATION_ID_EXTRA);

        if (paginationId instanceof UUID) {
            return (UUID) paginationId;
        }

        return listModule.getId();
    }

    /**
     * Return the entire query string with '?' always prefixed. Never null.
     */
    protected String getQueryString() {
        return queryString == null
            ? "?"
            : '?' + queryString;
    }

    /**
     * Return the entire pagination parameter name.
     */
    protected String getPageParameterName() {
        return HttpIdPrefixedParametersProcessor.parameterName(PAGE_PARAMETER, getPaginationId(model));
    }

    /**
     * Return the current page number.
     */
    protected int getPage() {
        if (page == null) {
            page = pageParam.getFirstById(int.class, getPaginationId(model));
        }
        return page;
    }

    @Override
    protected void onCreate(ViewResponse response) {

        ItemStream itemStream = model.getItemStream();
        if (itemStream == null) {
            return;
        }

        itemStreamType = itemStream.getClass().getSimpleName();
        limit = model.getItemStream().getItemsPerPage(site, mainObject);

        if (getPage() > 0) {
            offset = itemStream.getItemsPerPage(site, mainObject) * (getPage() - 1);

        } else {
            page = 1;
        }

        hasNextPage = itemStream.hasMoreThan(site, mainObject, offset + limit);
    }

    public Iterable<? extends LinkView> getPaginationLinkViews() {
        ItemStream itemStream = model.getItemStream();

        if (itemStream == null) {
            return null;
        }

        List<LinkView> pagination = new ArrayList<>();

        if (getPage() > 1) {
            pagination.add(new LinkView.Builder()
                .body("Previous Page")
                .href(StringUtils.addQueryParameters(getQueryString(), getPageParameterName(), getPage() - 1))
                .build());
        }

        if (hasNextPage) {
            pagination.add(new LinkView.Builder()
                .body("Next Page")
                .href(StringUtils.addQueryParameters(getQueryString(), getPageParameterName(), getPage() + 1))
                .build());
        }

        return pagination;
    }

    public <T> Iterable<? extends T> getItems(Class<T> viewClass) {
        return Optional.ofNullable(model.getItemStream())
            .map(is -> createViews(
                viewClass,
                is.getItems(site, mainObject, offset, is.getItemsPerPage(site, mainObject))
                    .stream()
                    .map(this::wrapPromo)
                    .collect(Collectors.toList())))
            .orElse(null);
    }

    /**
     * Ensure that any Promotable has the opportunity to create its PromoWrapper
     */
    protected Object wrapPromo(Object item) {
        if (item instanceof Recordable) {
            Promotable promotableItem = State.getInstance(item).as(Promotable.class);

            if (promotableItem != null) {
                Promo promo = (Promo) ObjectType.getInstance(Promo.class).createObject(null);
                InternalPromoItem promoItem = (InternalPromoItem) ObjectType.getInstance(InternalPromoItem.class)
                    .createObject(null);
                promoItem.setItem(promotableItem);
                promo.setItem(promoItem);
                return promotableItem.createPromoWrapper(promo);
            }
        }
        return item;
    }

    public CharSequence getTitle() {
        // Plain text
        return model.getTitle();
    }

    @Override
    protected boolean shouldCreate() {
        return model.getItemStream() != null && model.getItemStream().hasMoreThan(site, mainObject, 0);
    }
}
