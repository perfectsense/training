package brightspot.core.tool;

import java.util.List;
import java.util.Optional;

import com.psddev.cms.db.RichTextElement;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.NodeVisitor;

public class RichTextElementObserverVisitor implements NodeVisitor {

    List<RichTextElementObserver> observers;

    public RichTextElementObserverVisitor(List<RichTextElementObserver> observers) {
        this.observers = observers;

        if (this.observers == null || this.observers.size() == 0) {
            throw new IllegalArgumentException("No RichTextElementObserver instances were provided!");
        }
    }

    @Override
    public void head(Node node, int i) {

        Optional.ofNullable(node)
            .filter(n -> n instanceof Element)
            .map(Element.class::cast)
            .map(RichTextElement::fromElement)
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
