
package brightspot.core.tool;

import java.util.List;
import java.util.Optional;

import com.psddev.cms.db.RichTextElement;
import com.psddev.dari.db.Database;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;

public class RichTextElementObserverVisitor implements NodeVisitor {

    List<RichTextElementObserver> observers;
    Database database;

    public RichTextElementObserverVisitor(Database database, List<RichTextElementObserver> observers) {
        this.observers = observers;
        this.database = database;

        if (this.observers == null || this.observers.size() == 0) {
            throw new IllegalArgumentException("No RichTextElementObserver instances were provided!");
        }

        if (this.database == null) {
            throw new IllegalArgumentException("No database provided!");
        }
    }

    @Override
    public void head(Node node, int i) {

        Optional.ofNullable(node)
                .filter(n -> n instanceof Element)
                .map(Element.class::cast)
                .map(e -> RichTextElement.fromElement(database, e))
                .ifPresent(richTextElement -> {
                    for (RichTextElementObserver observer : observers) {
                        observer.observe(richTextElement);
                    }
                });
    }

    @Override
    public void tail(Node node, int i) {

    }
}
