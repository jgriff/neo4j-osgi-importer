package org.neo4j.osgi.parser

import org.neo4j.osgi.importer.entity.PackageExport

/**
 * @author <a href="mailto:brdlmaier49@gmail.com">Bradley Maier</a>
 * @since 0.0.1
 */
object ExportStatement {

  //TODO - Deal with uses statements. This will require some updates to the model
  def parse(exportStatement: String): List[PackageExport] = {

    val packagesDirectivesAndAttributes = PackageImportExport.parsePackageImportExportStatment(exportStatement)
    val versions = packagesDirectivesAndAttributes._3.filter(_._1.equalsIgnoreCase("version"))
    val version = if (versions.isEmpty) "0.0.0" else versions(0)._2

    packagesDirectivesAndAttributes._1.map(
      new PackageExport()
        .setPackage(_)
        .setVersion(version)
    )
  }

}
