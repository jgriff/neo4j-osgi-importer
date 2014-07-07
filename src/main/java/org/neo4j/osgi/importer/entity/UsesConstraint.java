package org.neo4j.osgi.importer.entity;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
@NodeEntity
public class UsesConstraint {
    @GraphId
    private Long id;

    @RelatedTo(type = "BUNDLE", direction = Direction.OUTGOING)
    private Bundle bundle;

    @RelatedTo(type = "PACKAGE", direction = Direction.OUTGOING)
    private Package constrainedPackage;

    @RelatedTo(type = "USES", direction = Direction.OUTGOING)
    private Package packagedUsed;

    public UsesConstraint() { }

    /**
     * Constructs this constraint for the specified "uses" package.  The argument
     * passed to this constructor should be the package being used (not the package
     * being exported).  The relationship between (exported package)->(used package)
     * is created when you add this instance to a {@link org.neo4j.osgi.importer.entity.Package}.
     *
     * @param packagedUsed The package used.
     * @since 0.0.1
     */
    public UsesConstraint(Package packagedUsed) {
        this.packagedUsed = packagedUsed;
    }


    public Bundle getBundle() {
        return bundle;
    }

    public UsesConstraint setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public Package getConstrainedPackage() {
        return constrainedPackage;
    }

    public UsesConstraint setConstrainedPackage(Package constrainedPackage) {
        this.constrainedPackage = constrainedPackage;
        return this;
    }

    public Package getPackagedUsed() {
        return packagedUsed;
    }

    public UsesConstraint setPackagedUsed(Package packagedUsed) {
        this.packagedUsed = packagedUsed;
        return this;
    }
}

