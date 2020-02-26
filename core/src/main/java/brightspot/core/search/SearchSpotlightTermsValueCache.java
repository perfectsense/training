package brightspot.core.search;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.psddev.dari.db.Database;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.ValueCache;
import com.psddev.dari.util.Lazy;
import com.psddev.terms.Term;
import org.ahocorasick.trie.Trie;

public class SearchSpotlightTermsValueCache implements ValueCache<Map<String, Term>> {

    public static Term get(String phrase) {
        return ValueCache.get(SearchSpotlightTermsValueCache.class).get(phrase);
    }

    static final Lazy<Trie> TRIGGERS = new Lazy<Trie>() {

        @Override
        protected Trie create() throws Exception {
            return Trie.builder()
                .ignoreCase()
                .ignoreOverlaps()
                .onlyWholeWords()
                .addKeywords(ValueCache.get(SearchSpotlightTermsValueCache.class).keySet())
                .build();
        }
    };

    @Override
    public Map<String, Term> load(Database database) {
        List<Term> allTerms = Query.from(Term.class).selectAll();

        Map<String, Term> terms = new HashMap<>();

        allTerms.forEach(term ->
            term.getAllPhrases()
                .forEach(str ->
                    terms.put(str.toLowerCase(Locale.ENGLISH), term)));

        return terms;
    }

    @Override
    public boolean shouldInvalidate(Object changed) {
        if (changed instanceof Term) {
            TRIGGERS.reset();
            return true;
        }
        return false;
    }
}
