package org.neo4j.osgi.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
public class DefaultDirectoryBundleImporter implements DirectoryBundleImporter {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDirectoryBundleImporter.class);

    @Autowired
    private FileBundleImporter fileBundleImporter;

    @Override
    public void importBundlesInDirectory(File directory, boolean recursively) throws IOException, IllegalArgumentException {
        if (directory == null  ||  !directory.exists()) throw new IllegalArgumentException("Directory does not exist: " + directory);

        // scan the directory, parse for osgi bundles, and populate the database
        File[] files = directory.listFiles();
        if (files == null || files.length <= 0) throw new IllegalArgumentException("Directory is empty.");
        for (File file : files) {
            if (file.isDirectory() && recursively) {
                importBundlesInDirectory(file, recursively);
            } else if (file.exists() && file.getName().toLowerCase().endsWith(".jar")) {
                try {
                    fileBundleImporter.importBundle(file);
                } catch (Throwable t) {
                    LOG.error("Error inspecting bundle: " + file, t);
                }
            }
        }
    }
}
