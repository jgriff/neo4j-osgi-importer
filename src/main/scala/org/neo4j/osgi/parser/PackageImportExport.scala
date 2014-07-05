package org.neo4j.osgi.parser

import org.neo4j.osgi.importer.entity.Package

/**
 * Parses either an OSGi package import or export statement and returns a list
 * a tuple containing a list of the packages contained therein, a list of the key value pairs representing
 * the relevant directives, and a list of the key value pairs representing the relevant attributes.
 * Surrounding quotation marks will be stripped from the values of any attributes or directives.
 * @author <a href="mailto:brdlmaier49@gmail.com">Bradley Maier</a>
 * @since 0.0.1
 */
private object PackageImportExport {

  /*TODO Decide whether defining what are essentially key values pairs in a directive and attribute class is
  worth the added type information */
  def parsePackageImportExportStatment(statement: String): (List[Package], List[(String, String)], List[(String, String)]) = {
    val packageNamesAndParameters = statement.split(";")

    val packages = packageNamesAndParameters.filter(
      candidatePackage => !(isDirective(candidatePackage) || isAttribute(candidatePackage))
    ).toList

    val directives = packageNamesAndParameters.filter(isDirective _).toList

    val attributes = packageNamesAndParameters.filter(isAttribute _).toList

    return (
      packages.map(new Package(_)),
      directives.map(directive => (directive.split(":=")(0), directive.split(":=")(1).filterNot(_ == '"'))),
      attributes.map(attribute => (attribute.split("=")(0), attribute.split("=")(1).filterNot(_ == '"')))
      )

  }

  def isDirective(directive: String): Boolean = {
    directive.contains(":=")
  }

  def isAttribute(attribute: String): Boolean = {
    attribute.contains("=") && (!attribute.contains(":="))
  }


}
