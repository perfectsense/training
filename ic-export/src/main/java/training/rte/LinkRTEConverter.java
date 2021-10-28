package training.rte;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import brightspot.core.link.ExternalLink;
import brightspot.core.link.InternalLink;
import brightspot.core.link.Link;
import brightspot.core.link.LinkRichTextElement;
import brightspot.core.link.Target;
import brightspot.util.FixedMap;
import com.psddev.cms.db.RichTextElement;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import org.jsoup.nodes.Element;
import training.ExportUtils;

public class LinkRTEConverter extends RichTextConverter {

    @Override
    public void convert(Recordable parent, Element element) {
        RichTextElement rte = Objects.requireNonNull(RichTextElement.fromElement(parent, element));
        if (!(rte instanceof LinkRichTextElement)) {
            throw new IllegalArgumentException("Received " + rte.getClass().getName());
        }

        LinkRichTextElement linkRTE = (LinkRichTextElement) rte;

        Link link = linkRTE.getLink();
        if (link == null) {
            element.unwrap();
            return;
        }

        Map<String, String> attributes = new LinkedHashMap<>(linkRTE.toAttributes());
        attributes.put("link-data", ObjectUtils.toJson(FixedMap.of(
            "linkText", linkRTE.getLinkText(),
            "link", processLink(link),
            "_id", linkRTE.getId(),
            "_type", ExportUtils.getExportType(linkRTE)
        )));

        Element replacement = new Element("a");
        attributes.forEach(replacement::attr);

        // replacement updates childNodes so need a copy to prevent ConcurrentModificationException
        new ArrayList<>(element.childNodes()).forEach(replacement::appendChild);

        element.replaceWith(replacement);
    }

    private Map<String, Object> processLink(Link link) {
        if (link instanceof ExternalLink) {
            ExternalLink externalLink = (ExternalLink) link;

            return FixedMap.of(
                "url", externalLink.getUrl(),
                "target", Optional.ofNullable(externalLink.getTarget())
                    .map(Target::name)
                    .orElse(null),
                "attributes", processAttributes(externalLink),
                "_id", externalLink.getId().toString(),
                "_type", ExportUtils.getExportType(externalLink));

        } else if (link instanceof InternalLink) {
            InternalLink internalLink = (InternalLink) link;

            return FixedMap.of(
                "item", ExportUtils.buildRef(internalLink.getItem()),
                "anchor", internalLink.getAnchor(),
                "target", Optional.ofNullable(internalLink.getTarget())
                    .map(Target::name)
                    .orElse(null),
                "attributes", processAttributes(internalLink),
                "_id", internalLink.getId().toString(),
                "_type", ExportUtils.getExportType(internalLink));

        } else {
            throw new IllegalArgumentException("Unhandled Link type " + link.getClass().getName());
        }
    }

    private List<Map<?, ?>> processAttributes(Link link) {
        List<Map<?, ?>> attributes = link.getAttributes()
            .stream()
            .map(a -> FixedMap.of(
                "name", a.getName(),
                "value", a.getValue(),
                "_id", a.getId().toString(),
                "_type", ExportUtils.getExportType(a)))
            .collect(Collectors.toList());

        return attributes.isEmpty() ? null : attributes;
    }
}
