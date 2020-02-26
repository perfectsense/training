package brightspot.core.pkg;

public interface Packageable extends PackagePageElements {

    default PackageableData asPackageableData() {
        return as(PackageableData.class);
    }
}
