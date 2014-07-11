# Neo4j OSGi Importer
Scans a directory for OSGi bundles and imports them into a [Neo4j](http://neo4j.com) database, storing the bundle information and it's package import/export relationships.

[![Build Status](https://drone.io/github.com/jgriff/neo4j-osgi-importer/status.png)](https://drone.io/github.com/jgriff/neo4j-osgi-importer/latest)

## Build

This project uses [Spring Boot](https://github.com/spring-projects/spring-boot) and the Spring Boot gradle plugin.  So just:
```bash
$ ./gradlew installApp
```

## Run

First, you need neo4j running in standalone (we are currently hardcoded to access over REST at "http://localhost:7474/db/data").

Then just run the app using the launcher script Spring Boot generated at `/build/install/neo4j-osgi-importer`

```bash
$ cd build/install/neo4j-osgi-importer/bin
$ ./neo4j-osgi-importer /dir/to/scan/for/bundles
```
By default, it will not scan the directory recursively.  To scan sub-directories as well, pass true as the 2nd arg:
```bash
$ ./neo4j-osgi-importer /dir/to/scan/for/bundles true
```


Once it finishes, you should be able to open http://localhost:7474 and see all of the bundles and their package relationships.
It is looking for `Bundle-SymbolicName`, `Import-Package`, and `Export-Package` headers in the `META-INF/MANIFEST.MF`.

## Some Queries

### Which bundles are using SLF4J?
```
MATCH (b:Bundle)-[:IMPORTS]->(p:Package {name: "org.slf4j"})
RETURN b,p LIMIT 100;
```

### Which bundle(s) are providing SLF4J packages?
```
MATCH (provider:Bundle)-[:EXPORTS]->(p:Package {name: "org.slf4j"})
RETURN provider,p;
```

### What versions of SLF4J are provided?
```
MATCH (provider:Bundle)-[e:EXPORTS]->(p:Package {name: "org.slf4j"})
RETURN provider.bsn,e.version;
```

### Which bundles can use SLF4J version 1.7?
```
MATCH (b:Bundle)-[i:IMPORTS]->(:Package {name:"org.slf4j"})
WHERE i.minVersion <= "1.7" AND i.maxVersion >= "1.7"
RETURN b,i;
```

### Which bundles do not specify a version range for a package import?
```
MATCH (b:Bundle)-[i:IMPORTS]->(p:Package)
WHERE toInt(i.minVersion) = 0 AND i.maxVersion IS NULL
RETURN b,i,p LIMIT 10;
```

### Which bundles have incompatible package imports?
```
MATCH (b1:Bundle)-[i1:IMPORTS]->(p:Package)<-[i2:IMPORTS]-(b2:Bundle)
WHERE i1.maxVersion < i2.minVersion
RETURN b1,p,b2 LIMIT 10;
```

### What are the top 10 most imported packages?
```
MATCH (:Bundle)-[i:IMPORTS]->(p:Package)
RETURN p.name, count(i) as importCnt
ORDER BY importCnt DESC LIMIT 10
```
### Which bundles import/export packages to each other?
```
MATCH (b1:Bundle)-[:IMPORTS]->(p1:Package)<-[:EXPORTS]-(b2:Bundle)
WHERE (b1)-[:EXPORTS]->(:Package)<-[:IMPORTS]-(b2)
RETURN b1,p1,b2 LIMIT 10;
```
