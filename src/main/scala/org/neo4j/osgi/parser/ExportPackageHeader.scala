package org.neo4j.osgi.parser

import java.util

import org.neo4j.osgi.importer.entity.PackageExport
import scala.collection.JavaConversions._

/**
 * Parses OSGi Export-Package Headers
 * @author <a href="mailto:brdlmaier49@gmail.com">Bradley Maier</a>
 * @since 0.0.1
 */
object ExportPackageHeader {

  def parse(exportPackageHeader: String): java.util.List[PackageExport] = {

    if (exportPackageHeader == null) {
      new util.ArrayList[PackageExport]()
    } else {
      val exportLists = for (packageExport <- exportPackageHeader.split(
        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)" //Regex to split on commas that aren't in quotes
      ).filterNot(_ == "")) yield
        ExportStatement.parse(packageExport)
      exportLists.flatten[PackageExport].toList
    }
  }
}
