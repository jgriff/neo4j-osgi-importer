package org.neo4j.osgi.importer.entity;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
@NodeEntity
public class Package {
    @GraphId private Long id;

    @Indexed(unique = true)
    private String name;

    @RelatedTo(type = "PACKAGE", direction = Direction.INCOMING)
    private Set<UsesConstraint> usesConstraints;

    public Package() { }

    public Package(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Package setName(String name) {
        this.name = name;
        return this;
    }

    public Set<UsesConstraint> getUsesConstraints() {
        return usesConstraints;
    }

    public Package setUsesConstraints(Set<UsesConstraint> usesConstraints) {
        this.usesConstraints = usesConstraints;
        return this;
    }

    public Package addUsesConstraint(UsesConstraint usesConstraint) {
        if (this.usesConstraints == null) {
            this.usesConstraints = new HashSet<UsesConstraint>();
        }
        usesConstraint.setConstrainedPackage(this);
        this.usesConstraints.add(usesConstraint);
        return this;
    }
}
