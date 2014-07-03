package org.neo4j.osgi.importer.entity;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
@NodeEntity
public class Package {
    @GraphId private Long id;

    @Indexed(unique = true)
    private String name;

    public Package() { }

    public Package(String name) {
        this.name = name;
    }
}
