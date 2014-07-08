# Neo4j OSGi Importer
Scans a directory for OSGi bundles and imports them into a [Neo4j](http://neo4j.com) database, storing the bundle information and it's package import/export relationships.

[![Build Status](https://drone.io/github.com/jgriff/neo4j-osgi-importer/status.png)](https://drone.io/github.com/jgriff/neo4j-osgi-importer/latest)

## How to Use

There are no config options _yet_, so you must edit `org.neo4j.osgi.importer.Application` for your environment.  It's pretty self-explanatory, just edit the directory (that's currently hardcoded) to scan.  Just change it to the directory you want to parse for OSGi bundles.

```java
    public static void main(String[] args) throws IOException {
        // TODO accept param args
        SpringApplication.run(Application.class, args)
                .getBean(DirectoryImporter.class)
                .importBundlesInDirectory(
                        new File("/your/directory/to/scan/for/bundles"),
                        false
                );
    }
```

## Running

First, you need neo4j running in standalone (we'll access it over REST).

Then just run Spring Boot via Gradle:

```bash
  ./gradlew bootRun
```

Once it finishes, you should be able to open http://localhost:7474 and see all of the bundles.
It is actually scanning the bundles and reading their `Bundle-SymbolicName`, `Import-Package`, and `Export-Package` headers.
