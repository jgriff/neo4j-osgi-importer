package org.neo4j.osgi.importer.entity;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.util.*;

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
@NodeEntity
public class Bundle {
    @GraphId private Long id;

    private String bsn;
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

    public String getBundleSymbolicName() {
        return bsn;
    }

    public Bundle setBundleSymbolicName(String bundleSymbolicName) {
        this.bsn = bundleSymbolicName;
        if (version != null) {
            bsnAndVersion = bundleSymbolicName + ":" + version;
        }
        return this;
    }

    public String getVersion() {
        return version;
    }

    public Bundle setVersion(String version) {
        this.version = version;
        if (bsn != null) {
            bsnAndVersion = bsn + ":" + version;
        }
        return this;
    }

    public Set<PackageImport> getPackageImports() {
        return packageImports;
    }

    public Bundle addPackageImport(PackageImport packageImport) {
        if (packageImports == null) {
            packageImports = new HashSet<PackageImport>();
        }
        if (packageImport != null) {
            packageImports.add(packageImport.setBundle(this));
        }
        return this;
    }

    public Bundle addPackageImports(Collection<PackageImport> packageImports) {
        for (PackageImport packageImport : packageImports) {
            addPackageImport(packageImport);
        }
        return this;
    }

    public Set<PackageExport> getPackageExports() {
        return packageExports;
    }

    public Bundle addPackageExport(PackageExport packageExport) {
        if (packageExports == null) {
            packageExports = new HashSet<PackageExport>();
        }
        if (packageExport != null) {
            packageExports.add(packageExport.setBundle(this));
        }
        return this;
    }

    public Bundle addPackageExports(Collection<PackageExport> packageExports) {
        for (PackageExport packageExport : packageExports) {
            addPackageExport(packageExport);
        }
        return this;
    }

    public Collection<? extends Package> getImportedPackages() {
        List<Package> toReturn = new ArrayList<Package>();
        if (packageImports != null) {
            for (PackageImport packageImport : packageImports) {
                Package pkg = packageImport.getPackage();
                if (pkg != null) {
                    toReturn.add(pkg);
                }
            }
        }
        return toReturn;
    }

    public Collection<? extends Package> getExportedPackages() {
        List<Package> toReturn = new ArrayList<Package>();
        if (packageExports != null) {
            for (PackageExport packageExport : packageExports) {
                Package pkg = packageExport.getPackage();
                if (pkg != null) {
                    toReturn.add(pkg);
                }
            }
        }
        return toReturn;
    }

    @Override
    public String toString() {
        return "Bundle{" +
                "bundleSymbolicName='" + bsn + '\'' +
                ", version='" + version + '\'' +
                ", packageImports=" + packageImports +
                ", packageExports=" + packageExports +
                '}';
    }

    public static class Builder {
        private String bundleSymbolicName;
        private String version;
        private Set<PackageImport> packageImports;
        private Set<PackageExport> packageExports;

        public Builder bundleSymbolicName(String bundleSymbolicName) {
            this.bundleSymbolicName = bundleSymbolicName;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder packageImport(PackageImport packageImport) {
            if (this.packageImports == null) {
                this.packageImports = new HashSet<PackageImport>();
            }
            this.packageImports.add(packageImport);
            return this;
        }

        public Builder packageImports(Collection<PackageImport> packageImports) {
            if (this.packageImports == null) {
                this.packageImports = new HashSet<PackageImport>();
            }
            this.packageImports.addAll(packageImports);
            return this;
        }

        public Builder packageExport(PackageExport packageExport) {
            if (this.packageExports == null) {
                this.packageExports = new HashSet<PackageExport>();
            }
            if (packageExport != null) {
                this.packageExports.add(packageExport);
            }
            return this;
        }

        public Builder packageExports(Collection<PackageExport> packageExports) {
            if (this.packageExports == null) {
                this.packageExports = new HashSet<PackageExport>();
            }
            if (packageExports != null) {
                this.packageExports.addAll(packageExports);
            }
            return this;
        }

        public Bundle build() {
            return new Bundle()
                    .setBundleSymbolicName(bundleSymbolicName)
                    .setVersion(version)
                    .addPackageImports(packageImports)
                    .addPackageExports(packageExports);
        }
    }
}
