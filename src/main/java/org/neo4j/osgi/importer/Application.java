package org.neo4j.osgi.importer;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

import java.io.File;

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
@Configuration
@EnableNeo4jRepositories
@EnableAutoConfiguration
public class Application extends Neo4jConfiguration {
    public Application() {
        setBasePackage("org.neo4j.osgi.importer");
    }

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new SpringRestGraphDatabase("http://localhost:7474/db/data");
//        return new GraphDatabaseFactory().newEmbeddedDatabase("build/hello.db");
    }

    @Bean
    public Runnable importer() {
        // TODO accept param args
        return new BundleDirectoryImporter(new File("/Users/griff/dev/sf2/sf2/assembly/target/sf2-3.2.0-SNAPSHOT/repository/sf2"));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).getBean(Runnable.class).run();
    }
}
