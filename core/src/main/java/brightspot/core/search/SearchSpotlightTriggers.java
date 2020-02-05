package brightspot.core.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.psddev.terms.Term;
import org.ahocorasick.trie.Token;

public class SearchSpotlightTriggers {

    public static List<Term> getTriggeredTerms(String queryString) {
        List<Term> terms = new ArrayList<>();
        for (Token token : SearchSpotlightTermsValueCache.TRIGGERS.get()
            .tokenize(queryString.toLowerCase(Locale.ENGLISH))) {
            String fragment = token.getFragment();
            Term term = fragment.length() > 1 ? SearchSpotlightTermsValueCache.get(fragment) : null;
            if (term != null) {
                terms.add(term);
            }
        }
        return terms;
    }

}
