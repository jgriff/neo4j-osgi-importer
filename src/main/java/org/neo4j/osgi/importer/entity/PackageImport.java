package org.neo4j.osgi.importer.entity;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
@RelationshipEntity(type = "IMPORTS")
public class PackageImport {

    @GraphId private Long id;
    /** The bundle importing the package. */
    @StartNode private Bundle bundle;
    /** The package being imported. */
    @EndNode private Package pakage;

    private String minVersion;
    private boolean minVersionInclusive = true;
    private String maxVersion;
    private boolean maxVersionInclusive = false; // exclusive by default

    public PackageImport setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public Package getPakage() {
        return pakage;
    }

    public PackageImport setPackage(Package pakage) {
        this.pakage = pakage;
        return this;
    }

    public PackageImport setMinVersion(String minVersion) {
        this.minVersion = minVersion;
        return this;
    }

    public PackageImport setMinVersionInclusive(boolean minVersionInclusive) {
        this.minVersionInclusive = minVersionInclusive;
        return this;
    }

    public PackageImport setMaxVersion(String maxVersion) {
        this.maxVersion = maxVersion;
        return this;
    }

    public PackageImport setMaxVersionInclusive(boolean maxVersionInclusive) {
        this.maxVersionInclusive = maxVersionInclusive;
        return this;
    }
}
