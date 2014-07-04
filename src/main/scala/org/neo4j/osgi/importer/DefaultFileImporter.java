package org.neo4j.osgi.importer;

import org.neo4j.osgi.importer.entity.Bundle;
import org.neo4j.osgi.importer.entity.Package;
import org.neo4j.osgi.importer.entity.PackageExport;
import org.neo4j.osgi.importer.entity.PackageImport;
import org.neo4j.osgi.importer.repository.BundleRepository;
import org.neo4j.osgi.importer.repository.PackageRepository;
import org.neo4j.osgi.parser.Import;
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
 * The default {@link org.neo4j.osgi.importer.FileImporter}, backed by a scala
 * {@link org.neo4j.osgi.parser.Import}.
 *
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
public class DefaultFileImporter implements FileImporter {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultFileImporter.class);

    @Autowired
    private BundleRepository bundleRepository;
    @Autowired
    private PackageRepository packageRepository;

    public void importBundle(File file) throws IOException {
        if (file == null  ||  !file.exists()) throw new IllegalArgumentException("File does not exist: " + file);


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

            if (StringUtils.hasLength(bsn) && StringUtils.hasLength(version)) {

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

    private <T> List<T> toList(scala.collection.immutable.List<T> scalaList) {
        List<T> list = scala.collection.JavaConverters.asJavaListConverter(scalaList).asJava();
        return list;
    }

    private List<PackageImport> parseImportPackageStatement(String importPackage) {
        return toList(Import.parse(importPackage));
    }

    private List<PackageExport> parseExportPackageStatement(String exportPackage) {
        // TODO need scala variant for package exports as well
        List<PackageExport> packageExports = new ArrayList<PackageExport>();

//        final int pkgCnt = (int) (Math.random() * 6 + 1);
//        for (int i = 0; i < pkgCnt; i++) {
//            packageExports.add(new PackageExport()
//                    .setVersion("1.5.0")
//                    .setPackage(randomPackage()));
//        }

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
