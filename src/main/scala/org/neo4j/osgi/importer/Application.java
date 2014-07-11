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
import java.io.IOException;

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

    @Bean public DirectoryBundleImporter directoryImporter() { return new DefaultDirectoryBundleImporter(); }
    @Bean public FileBundleImporter fileImporter() { return new DefaultFileBundleImporter(); }

    public static void main(String[] args) throws IOException {
        if (args.length <= 0) throw new IllegalArgumentException("Please specify (as first arg) the directory to scan for OSGi bundles." +
        "  You can optinally specify true/false as the second arg, to indicate whether the directory should be scanned recursively (default is false).");
        boolean recursive = (args.length > 1 && Boolean.valueOf(args[1]));

        SpringApplication.run(Application.class, args)
                .getBean(DirectoryBundleImporter.class)
                .importBundlesInDirectory( new File(args[0]), recursive );
    }
}
