package org.neo4j.osgi.parser

import java.util

import org.neo4j.osgi.importer.entity.PackageImport
import scala.collection.JavaConversions._
import scala.collection.mutable

/**
 * Parses Import-Package headers
 * @author <a href="mailto:brdlmaier49@gmail.com">Bradley Maier</a>
 * @since 0.0.1
 */
object ImportPackageHeader {


  def parse(header: String): java.util.List[PackageImport] = {
    if (header == null) {
      new util.ArrayList[PackageImport] {}
    } else {
      val importLists = for (packageImport <- header.split(
        ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)" //Regex to split on commas that aren't in quotes
      ).filterNot(_ == "")) yield
        ImportStatement.parse(packageImport)

      importLists.flatten[PackageImport].toList
    }
  }


}
