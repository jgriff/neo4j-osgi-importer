package org.neo4j.osgi.parser

import org.neo4j.osgi.importer.entity.PackageImport
import scala.collection.JavaConversions._

/**
 * Parses Import-Package headers
 * @author <a href="mailto:brdlmaier49@gmail.com">Bradley Maier</a>
 * @since 0.0.1
 */
object ImportPackage {


  def parse(header: String): java.util.List[PackageImport] = {
    val importLists = for (packageImport <- header.split(
      ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)" //Regex to split on commas that aren't in quotes
    ).filterNot(_ == "")) yield
      Import.parse(packageImport)
    importLists.flatten[PackageImport].toList
  }


}
