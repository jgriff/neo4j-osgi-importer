package org.neo4j.osgi.importer;

import java.io.File;
import java.io.IOException;

/**
 * File-based importer that imports zero or more bundles from a directory.
 *
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
public interface DirectoryBundleImporter {
    /**
     * Looks in the specified directory and imports all files that are OSGi bundles.
     *
     * @param directory The directory to look for OSGi bundles in.
     * @param recursively Specify <code>true</code> to recurse into subdirectories.
     * @throws java.io.IOException Raised for any error listing the files in the directory.
     * @throws IllegalArgumentException Raised if the specified {@link File} object is not a valid directory.
     * @since 0.0.1
     */
    void importBundlesInDirectory(File directory, boolean recursively) throws IOException, IllegalArgumentException;
}
