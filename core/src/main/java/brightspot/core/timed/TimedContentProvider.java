package brightspot.core.timed;

/**
 * Objects that reference {@linkplain TimedContent} or know how to create them.
 */
public interface TimedContentProvider {

    TimedContent getTimedContent();
}
