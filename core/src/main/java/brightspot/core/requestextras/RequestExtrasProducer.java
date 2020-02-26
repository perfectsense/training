package brightspot.core.requestextras;

import java.util.List;

import com.psddev.cms.db.Site;

/**
 * Produce a list of {@link RequestExtra}s found using the Site and MainObject.
 *
 * Note that these objects will not have been filtered against the request yet; use {@link RequestExtras#findExtras()}
 * for that.
 */
public interface RequestExtrasProducer<T extends RequestExtra> {

    List<T> produce(Site site, Object mainObject);

}
