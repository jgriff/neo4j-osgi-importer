package org.neo4j.osgi.importer.entity;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

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

    @Fetch
    @RelatedToVia(type = "USES", direction = Direction.OUTGOING)
    private Set<Uses> usesPackages = new HashSet<Uses>();

    public Package() {}

    public Package(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Uses> getUsesPackages() {
        return usesPackages;
    }

    public Package setUsesPackages(Set<Uses> usesPackages) {
        this.usesPackages = usesPackages;
        return this;
    }
}
