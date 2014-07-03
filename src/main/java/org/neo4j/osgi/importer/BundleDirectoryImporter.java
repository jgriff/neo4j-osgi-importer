package org.neo4j.osgi.importer;

import org.neo4j.osgi.importer.entity.Bundle;
import org.neo4j.osgi.importer.entity.Package;
import org.neo4j.osgi.importer.entity.PackageExport;
import org.neo4j.osgi.importer.entity.PackageImport;
import org.neo4j.osgi.importer.repository.BundleRepository;
import org.neo4j.osgi.importer.repository.PackageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
public class BundleDirectoryImporter implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(BundleDirectoryImporter.class);

    private final File directory;
    @Autowired
    private BundleRepository bundleRepository;
    @Autowired
    private PackageRepository packageRepository;

    public BundleDirectoryImporter(File directory) {
        this.directory = directory;
    }

    @Override
    public void run() {
        // TODO scan the directory, parse for osgi bundles, and populate the database
        for (File file : directory.listFiles()) {
            if (!file.isDirectory() && file.getName().toLowerCase().endsWith(".jar")) {
                try {
                    importBundle(file);
                } catch (IOException e) {
                    LOG.error("Error inspecting bundle: " + file, e);
                }
            }
        }

//        Package pkg = new Package("com.baz");
//        packageRepository.save(pkg);
//
//        Bundle b1 = new Bundle()
//                .setBundleSymbolicName("com.foo")
//                .setVersion("1.0.0.M1")
//                .addPackageImport(new PackageImport()
//                        .setMinVersion("1.0.0")
//                        .setMaxVersion("2.0.0")
//                        .setPackage(pkg));
//
//        bundleRepository.save(b1);
    }

    private void importBundle(File file) throws IOException {
        LOG.trace("Inspecting bundle: " + file);
        JarInputStream jarStream = new JarInputStream(new FileInputStream(file));
        Manifest mf = jarStream.getManifest();

        Attributes attrs = mf.getMainAttributes();
        if (attrs != null) {
            String bsn = attrs.getValue("Bundle-SymbolicName");
            LOG.trace("Bundle-SymbolicName: " + bsn);

            String version = attrs.getValue("Bundle-Version");
            LOG.trace("Version: " + version);

            String importPackage = attrs.getValue("Import-Package");
            LOG.trace("Import-Package: " + importPackage);

            String exportPackage = attrs.getValue("Export-Package");
            LOG.trace("Export-Package: " + exportPackage);

            if (StringUtils.hasLength(bsn) &&
                    StringUtils.hasLength(version)) {

                List<PackageImport> packageImports = parseImportPackageStatement(importPackage);
                List<PackageExport> packageExports = parseExportPackageStatement(exportPackage);

                // need to first save the packages in the relationship (not sure why yet)
                List<Package> packages = new ArrayList<Package>();
                for (PackageImport packageImport : packageImports) {
                    packages.add(packageImport.getPakage());
                }
                for (PackageExport packageExport : packageExports) {
                    packages.add(packageExport.getPakage());
                }
                packageRepository.save(packages);

                // now save the bundle and it's relationships
                bundleRepository.save(new Bundle()
                                .setBundleSymbolicName(bsn)
                                .setVersion(version)
                                .addPackageImports(packageImports)
                                .addPackageExports(packageExports)
                );
            }
        }
    }

    // TODO look into using bndtools (https://github.com/bndtools/bndtools) to do the heavy lifting parsing the MANIFEST.MF for this info
    private List<PackageImport> parseImportPackageStatement(String importPackage) {
        // for now, generate some dummy package imports to show proof of concept
        List<PackageImport> packageImports = new ArrayList<PackageImport>();
        final int pkgCnt = (int) (Math.random() * 6 + 1);
        for (int i = 0; i < pkgCnt; i++) {
            packageImports.add(new PackageImport()
                .setMinVersion("1.0.0")
                .setMaxVersion("2.0.0")
                .setPackage(randomPackage()));
        }

        return packageImports;
    }

    private List<PackageExport> parseExportPackageStatement(String exportPackage) {
        // for now, generate some dummy package imports to show proof of concept
        List<PackageExport> packageExports = new ArrayList<PackageExport>();
        final int pkgCnt = (int) (Math.random() * 6 + 1);
        for (int i = 0; i < pkgCnt; i++) {
            packageExports.add(new PackageExport()
                    .setVersion("1.5.0")
                    .setPackage(randomPackage()));
        }

        return packageExports;
    }

    private Package randomPackage() {
        switch ((int) (Math.random() * 6 + 1)) {
            case 1: return new Package("org.sweet.sassy");
            case 2: return new Package("org.this.aint.gonna.work");
            case 3: return new Package("com.4rsmokehouse");
            case 4: return new Package("gov.wannabe");
            case 5: return new Package("super.duper.guadalupe");
            case 6: return new Package("edu.ucf.cs");
            case 7: return new Package("org.make.my.day");
            default: return new Package("some.other.package");
        }
    }
}
