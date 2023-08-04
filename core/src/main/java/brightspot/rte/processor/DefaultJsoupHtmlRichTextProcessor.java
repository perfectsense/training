package brightspot.rte.processor;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import brightspot.rte.importer.RichTextImporterConfiguration;
import brightspot.rte.importer.html.jsoup.JsoupHtmlRichTextProcessor;
import brightspot.rte.importer.html.jsoup.JsoupHtmlTagProcessor;
import com.psddev.dari.util.ClassFinder;
import com.psddev.dari.util.TypeDefinition;

/**
 * Default implementation of {@link JsoupHtmlRichTextProcessor}.
 */
public class DefaultJsoupHtmlRichTextProcessor implements JsoupHtmlRichTextProcessor {

    @Override
    public Set<? extends JsoupHtmlTagProcessor> getHtmlTagProcessors() {
        return ClassFinder.findConcreteClasses(JsoupHtmlTagProcessor.class).stream()
                .map(c -> TypeDefinition.getInstance(c).newInstance())
                .collect(Collectors.toSet());
    }

    @Override
    public Map<String, Queue<JsoupHtmlTagProcessor>> getProcessorQueues() {
        return JsoupHtmlRichTextProcessor.super.getProcessorQueues();
    }

    @Override
    public boolean isSupported(RichTextImporterConfiguration<?> configuration) {
        return JsoupHtmlRichTextProcessor.super.isSupported(configuration);
    }
}
