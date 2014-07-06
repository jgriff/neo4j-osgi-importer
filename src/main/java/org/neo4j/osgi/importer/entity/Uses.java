package org.neo4j.osgi.importer.entity;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

/**
 * @author <a href="mailto:brdlmaier49@gmail.com">Bradley Maier</a>
 * @since 0.0.1
 */
@RelationshipEntity(type = "USES")
public class Uses {
    @GraphId private Long id;
    /** The package constraining use */
    @StartNode private Package pakage;
    /** The package being used */
    @EndNode private Package uses;


    public Package getPakage() {return pakage;}

    public Uses setPakage(Package pakage) {
        this.pakage = pakage;
        return this;
    }

    public Package getUses() {return uses; }

    public Uses setUses(Package uses) {
        this.uses = uses;
        return this;
    }
}
