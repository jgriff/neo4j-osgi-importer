package org.neo4j.osgi.importer.entity;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
@NodeEntity
public class Bundle {
    @GraphId private Long id;

    private String bundleSymbolicName;
    private String version;

    // temporary hack to support "BSN + version = uniqueness"
    @Indexed(unique = true)
    private String bsnAndVersion;

    @Fetch
    @RelatedToVia(type = "IMPORTS", direction = Direction.OUTGOING)
    private Set<PackageImport> packageImports;

    @Fetch
    @RelatedToVia(type = "EXPORTS", direction = Direction.OUTGOING)
    private Set<PackageExport> packageExports;

    public Bundle setBundleSymbolicName(String bundleSymbolicName) {
        this.bundleSymbolicName = bundleSymbolicName;
        if (version != null) {
            bsnAndVersion = bundleSymbolicName + ":" + version;
        }
        return this;
    }

    public Bundle setVersion(String version) {
        this.version = version;
        if (bundleSymbolicName != null) {
            bsnAndVersion = bundleSymbolicName + ":" + version;
        }
        return this;
    }

    public Bundle addPackageImport(PackageImport packageImport) {
        if (packageImports == null) {
            packageImports = new HashSet<PackageImport>();
        }
        packageImports.add(packageImport.setBundle(this));
        return this;
    }

    public Bundle addPackageImports(Collection<PackageImport> packageImports) {
        for (PackageImport packageImport : packageImports) {
            addPackageImport(packageImport);
        }
        return this;
    }

    public Bundle addPackageExport(PackageExport packageExport) {
        if (packageExports == null) {
            packageExports = new HashSet<PackageExport>();
        }
        packageExports.add(packageExport.setBundle(this));
        return this;
    }

    public Bundle addPackageExports(Collection<PackageExport> packageExports) {
        for (PackageExport packageExport : packageExports) {
            addPackageExport(packageExport);
        }
        return this;
    }
}
