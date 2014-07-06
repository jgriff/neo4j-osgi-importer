package org.neo4j.osgi.importer;

import org.neo4j.osgi.importer.entity.*;
import org.neo4j.osgi.importer.entity.Package;
import org.neo4j.osgi.importer.repository.BundleRepository;
import org.neo4j.osgi.importer.repository.PackageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

/**
 * Abstract {@link FileImporter}, leaving complex parsing operations to subclasses.
 *
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
public abstract class AbstractFileImporter implements FileImporter {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFileImporter.class);

    @Autowired
    private BundleRepository bundleRepository;
    @Autowired
    private PackageRepository packageRepository;

    public void importBundle(File file) throws IOException {
        if (file == null || !file.exists()) throw new IllegalArgumentException("File does not exist: " + file);

        LOG.trace("Inspecting bundle: " + file);
        JarInputStream jarStream = new JarInputStream(new FileInputStream(file));
        Manifest mf = jarStream.getManifest();

        if (mf == null) throw new IllegalArgumentException("No Manifest found for: " + file);

        Attributes attrs = mf.getMainAttributes();
        if (attrs == null) throw new IllegalArgumentException("No attributes found in Manifest for: " + file);

        Bundle bundle = new Bundle.Builder()
                .bundleSymbolicName(parseBundleSymbolicName(attrs.getValue("Bundle-SymbolicName")))
                .version(parseVersion(attrs.getValue("Bundle-Version")))
                .packageImports(parseImportPackage(attrs.getValue("Import-Package")))
                .packageExports(parseExportPackage(attrs.getValue("Export-Package")))
                .build();
        LOG.debug("Parsed bundle information for '" + file + "' as: " + bundle);

        // need to first save the packages in the relationship (not sure why yet)
        List<Package> packages = new ArrayList<Package>();
        packages.addAll(bundle.getImportedPackages());
        packages.addAll(bundle.getExportedPackages());
        packages.addAll(bundle.getUsedPackages());
        List<Set<Uses>> holdUses = stripUses(packages);
        packageRepository.save(packages);
        replaceUses(holdUses, packages);
        packageRepository.save(packages);
        // now save the bundle and it's relationships
        bundleRepository.save(bundle);
    }

    private List<Set<Uses>> stripUses(List<Package> packages) {
        List<Set<Uses>> uses = new ArrayList<Set<Uses>>();
        for (Package pakage : packages) {
            uses.add(pakage.getUsesPackages());
            pakage.setUsesPackages(new HashSet<Uses>());
        }
        return uses;
    }

    private void replaceUses(List<Set<Uses>> holdUses, List<Package> packages) {
        int i = 0;
        for (Package pakage: packages) {
            pakage.setUsesPackages(holdUses.get(i));
            i++;
        }
    }

    protected String parseBundleSymbolicName(String bsn) { return bsn; }
    protected String parseVersion(String version) { return version; }
    protected abstract Collection<PackageImport> parseImportPackage(String importPackage);
    protected abstract Collection<PackageExport> parseExportPackage(String exportPackage);
}
