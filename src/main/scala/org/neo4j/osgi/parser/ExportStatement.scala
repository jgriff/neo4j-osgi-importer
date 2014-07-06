package org.neo4j.osgi.parser

import java.util

import org.neo4j.osgi.importer.entity.{Uses, PackageExport, Package}
import scala.collection.JavaConversions._

/**
 * @author <a href="mailto:brdlmaier49@gmail.com">Bradley Maier</a>
 * @since 0.0.1
 */
object ExportStatement {

  def parse(exportStatement: String): List[PackageExport] = {

    val packagesDirectivesAndAttributes = PackageImportExport.parsePackageImportExportStatment(exportStatement)
    val versions = packagesDirectivesAndAttributes._3.filter(_.key.equalsIgnoreCase("version"))
    val version = if (versions.isEmpty) "0.0.0" else versions(0).value
    val usesDirectives = packagesDirectivesAndAttributes._2.filter(_.key.equalsIgnoreCase("uses"))
    val uses: util.Set[Package] = if (usesDirectives.isEmpty) {
      new util.HashSet[Package]()
    } else {
      getUsesPackages(usesDirectives(0).value.split(','))
    }

    packagesDirectivesAndAttributes._1.map(pakage => {
      val usesForPakage = uses.map(
        new Uses()
          .setUses(_)
          .setPakage(pakage)
      )

      new PackageExport()
        .setPackage(pakage.setUsesPackages(usesForPakage))
        .setVersion(version)
    })
  }

  def getUsesPackages(packageNames: Array[String]): java.util.Set[Package] = {
    packageNames
      .map(new Package(_))
      .toSet[Package]

  }

}
