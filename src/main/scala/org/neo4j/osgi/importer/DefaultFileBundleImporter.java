package org.neo4j.osgi.importer;

import org.neo4j.osgi.importer.entity.PackageExport;
import org.neo4j.osgi.importer.entity.PackageImport;
import org.neo4j.osgi.parser.ExportPackageHeader;
import org.neo4j.osgi.parser.ImportPackageHeader;

import java.util.Collection;

/**
 * A {@link FileBundleImporter} backed by parsers in the <code>org.neo4j.osgi.parser</code> package.
 *
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
public class DefaultFileBundleImporter extends AbstractFileBundleImporter {

    @Override
    protected Collection<PackageImport> parseImportPackage(String importPackage) {
        return ImportPackageHeader.parse(importPackage);
    }

    @Override
    protected Collection<PackageExport> parseExportPackage(String exportPackage) {
        return ExportPackageHeader.parse(exportPackage);
    }
}
