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
public class DefaultDirectoryImporter implements DirectoryImporter {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDirectoryImporter.class);

    @Autowired
    private FileImporter fileImporter;

    @Override
    public void importBundlesInDirectory(File directory, boolean recursively) throws IOException, IllegalArgumentException {
        if (directory == null  ||  !directory.exists()) throw new IllegalArgumentException("Directory does not exist: " + directory);

        // scan the directory, parse for osgi bundles, and populate the database
        for (File file : directory.listFiles()) {
            if (file.isDirectory() && recursively) {
                importBundlesInDirectory(directory, recursively);
            } else if (file.exists() && file.getName().toLowerCase().endsWith(".jar")) {
                try {
                    fileImporter.importBundle(file);
                } catch (Throwable t) {
                    LOG.error("Error inspecting bundle: " + file, t);
                }
            }
        }
    }
}
