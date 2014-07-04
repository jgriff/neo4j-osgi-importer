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
object Import {

  def parse(importStatement: String): List[PackageImport] = {
    val packageNamesAndParameters = importStatement.split(";")

    val packages = packageNamesAndParameters.filter(
        candidatePackage => !(isDirective(candidatePackage) || isAttribute(candidatePackage))
    )

    val resolutions = packageNamesAndParameters.filter(
        candidateResolution => isDirective(candidateResolution) && candidateResolution.toLowerCase.startsWith("resolution")
    )
    val resolution = if (resolutions.isEmpty) {
      "mandatory"
    } else {
      resolutions(0).split(":=")(1)
    }
    val versions = packageNamesAndParameters.filter(
        candidateVersion => isAttribute(candidateVersion) && candidateVersion.toLowerCase.startsWith("version")
    )

    val version = if(versions.isEmpty) {
      "0.0.0"
    } else {
      versions(0).split("=")(1).filterNot(_ == '"')
    }

    buildPackageImports(packages.toList, resolution, version)
  }

  def isDirective(directive: String): Boolean = {
    directive.contains(":=")
  }

  def isAttribute(attribute: String): Boolean = {
    attribute.contains("=") && (!attribute.contains(":="))
  }

  def buildPackageImports(packages: List[String],
                         resolution: String = "mandatory",
                         versionRange: String = "0.0.0"): List[PackageImport] = {
    val minVersion = versionRange.split(',')(0).filterNot(_ == '(').filterNot(_ == '[')
    val maxVersion = if (versionRange.split(',').length > 1) {
      versionRange.split(',')(1).filterNot(_ == ')').filterNot(_ == ']')
    } else null
    val minInclusive = !versionRange.startsWith("(") //Inclusive by default
    val maxInclusive = versionRange.endsWith("]") //Exclusive by default

    for (packageName <- packages) yield {
      new PackageImport()
        .setPackage(new Package(packageName))
        .setMandatory(resolution.equalsIgnoreCase("mandatory"))
        .setMinVersion(minVersion)
        .setMaxVersion(maxVersion)
        .setMinVersionInclusive(minInclusive)
        .setMaxVersionInclusive(maxInclusive)
    }
  }

}
