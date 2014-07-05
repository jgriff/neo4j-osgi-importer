package org.neo4j.osgi.importer;

import java.io.File;
import java.io.IOException;

/**
 * File-based bundle importer.  Imports a bundle and all of its relationships
 * from its raw file (.jar).
 *
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
public interface FileImporter {
    /**
     * If the specified file is an OSGi bundle, it is imported by harvesting all
     * available information in the file (such as the <code>MANIFEST.MF</code>).
     *
     * @param bundle The bundle file.
     * @throws IOException Raised for any error reading the file.
     * @throws IllegalArgumentException Raised if the specified {@link File} object is not a valid OSGi bundle,
     * or cannot be imported for any other reason.
     * @since 0.0.1
     */
    void importBundle(File bundle) throws IOException, IllegalArgumentException;
}
