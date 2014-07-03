package org.neo4j.osgi.importer.entity;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
@RelationshipEntity(type = "EXPORTS")
public class PackageExport {

    @GraphId private Long id;
    /** The bundle exporting the package. */
    @StartNode private Bundle bundle;
    /** The package being imported. */
    @EndNode private Package pakage;

    /** The version being exported. */
    private String version;

    public PackageExport setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public Package getPakage() {
        return pakage;
    }

    public PackageExport setPackage(Package pakage) {
        this.pakage = pakage;
        return this;
    }

    public PackageExport setVersion(String version) {
        this.version = version;
        return this;
    }
}
