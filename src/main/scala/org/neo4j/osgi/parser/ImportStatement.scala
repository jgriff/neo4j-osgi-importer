package org.neo4j.osgi.parser

import org.neo4j.cypher.internal.compiler.v1_9.commands.expressions.Collection
import org.neo4j.osgi.importer.entity.PackageImport
import org.neo4j.osgi.importer.entity.Package

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * Parse individual import statements into a list of package imports.
 * A list is necessary because a single import statement can list multiple package names
 * @author <a href="mailto:brdlmaier49@gmail.com">Bradley Maier</a>
 * @since 0.0.1
 */
object ImportStatement {

  def parse(importStatement: String): List[PackageImport] = {

    val packagesDirectivesAndAttributes = PackageImportExport.parsePackageImportExportStatment(importStatement)
    val resolutions = packagesDirectivesAndAttributes._2.filter(_._1.equalsIgnoreCase("resolution"))
    val resolution = if (resolutions.isEmpty) "mandatory" else resolutions(0)._2
    val versions = packagesDirectivesAndAttributes._3.filter(_._1.equalsIgnoreCase("version"))
    val version = if (versions.isEmpty) "0.0.0" else versions(0)._2

    buildPackageImports(packagesDirectivesAndAttributes._1, resolution, version)
  }


  def buildPackageImports(packages: List[Package],
                          resolution: String = "mandatory",
                          versionRange: String = "0.0.0"): List[PackageImport] = {
    val minVersion = versionRange.split(',')(0).filterNot(_ == '(').filterNot(_ == '[')
    val maxVersion = if (versionRange.split(',').length > 1) {
      versionRange.split(',')(1).filterNot(_ == ')').filterNot(_ == ']')
    } else null
    val minInclusive = !versionRange.startsWith("(") //Inclusive by default
    val maxInclusive = versionRange.endsWith("]") //Exclusive by default

    for (pakage <- packages) yield {
      new PackageImport()
        .setPackage(pakage)
        .setMandatory(resolution.equalsIgnoreCase("mandatory"))
        .setMinVersion(minVersion)
        .setMaxVersion(maxVersion)
        .setMinVersionInclusive(minInclusive)
        .setMaxVersionInclusive(maxInclusive)
    }
  }

}
