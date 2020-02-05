package brightspot.core.anchor;

public interface AnchorableWithOverrides extends Anchorable {

    default AnchorableData asAnchorableData() {
        return as(AnchorableData.class);
    }

    default String getAnchorableAnchorFallback() {
        return null;
    }

    @Override
    default String getAnchorableAnchor() {
        return asAnchorableData().getAnchor();
    }
}
