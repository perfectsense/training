package brightspot.rte.processor;

import java.util.Collections;
import java.util.Set;

import brightspot.rte.importer.DefaultRichTextImporter;
import brightspot.rte.importer.RichTextImporterConfiguration;
import brightspot.rte.importer.RichTextProcessor;

/**
 * Default implementation of {@link RichTextImporterConfiguration}.
 */
public class DefaultRichTextImporterConfiguration implements RichTextImporterConfiguration<DefaultRichTextImporter> {

    @Override
    public Set<? extends RichTextProcessor> getRichTextProcessors() {
        return Collections.singleton(new DefaultJsoupHtmlRichTextProcessor());
    }

    @Override
    public DefaultRichTextImporter buildImporter() {
        return new DefaultRichTextImporter(this);
    }
}
