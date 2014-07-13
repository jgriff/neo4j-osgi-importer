package org.neo4j.osgi.parser


import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.neo4j.osgi.importer.entity.{UsesConstraint, Package}

/**
 * @author <a href="mailto:brdlmaier49@gmail.com">Bradley Maier</a>
 * @since 0.0.1
 */

@RunWith(classOf[JUnitRunner])
class ExportStatementSpec extends FunSpec {
  describe("ExportStatement") {
    describe("Should parse a single package export statement") {
      it("Should produce an ImportPackage") {
        assert(ExportStatement.parse("my.package.name").length == 1)
      }

      it("Should have have the correct name and version") {
        val exportPackage = ExportStatement.parse("my.package.name")(0)
        assert(exportPackage.getPackage.getName == "my.package.name")
        assert(exportPackage.getVersion == "0.0.0")
      }

      it("Should correctly parse a version attribute") {
        val exportPackage = ExportStatement.parse("my.package.name;version=1.0.0")(0)
        assert(exportPackage.getPackage.getName == "my.package.name")
        assert(exportPackage.getVersion == "1.0.0")
      }

      it("Should handle other directives and attributes") {
        val exportPackage = ExportStatement.parse("my.package.name;version=2.0.0;resolution:=bananas;" +
          "users:=\"other.package.one,another.other.package\"")(0)
        assert(exportPackage.getPackage.getName == "my.package.name")
        assert(exportPackage.getVersion == "2.0.0")
      }

      it("Should handle multiple package names") {
        val exportPackages = ExportStatement.parse(
          "my.package.name;my.other.package.name;my.third.package.name;version=3.0.0;resolution:=bananas"
        )
        assert(exportPackages.length == 3)
        val myPackageExport = exportPackages(0)
        val myOtherPackageExport = exportPackages(1)
        val myThirdPackageExport = exportPackages(2)

        assert(myPackageExport.getPackage.getName == "my.package.name")
        assert(myPackageExport.getVersion == "3.0.0")

        assert(myOtherPackageExport.getPackage.getName == "my.other.package.name")
        assert(myOtherPackageExport.getVersion == "3.0.0")

        assert(myThirdPackageExport.getPackage.getName == "my.third.package.name")
        assert(myThirdPackageExport.getVersion == "3.0.0")
      }

      it("Should add uses statements to packages") {
        val exportPackages = ExportStatement.parse(
          "my.package.name;my.other.package.name;my.third.package.name;uses:=\"uses.this,uses.that\";version=3.0.0;resolution:=bananas"
        )
        assert(exportPackages.length == 3)
        val myPackageExport = exportPackages(0)
        val myOtherPackageExport = exportPackages(1)
        val myThirdPackageExport = exportPackages(2)

        assert(myPackageExport.getPackage.getName == "my.package.name")
        assert(myPackageExport.getVersion == "3.0.0")
        assert(myPackageExport.getPackage.getUsesConstraints.size() == 2)
        assert(myPackageExport.getPackage.getUsesConstraints.toArray.count(_.asInstanceOf[UsesConstraint].getPackageUsed.getName == "uses.this") == 1)
        assert(myPackageExport.getPackage.getUsesConstraints.toArray.count(_.asInstanceOf[UsesConstraint].getConstrainedPackage.getName == "my.package.name") == 2)
        assert(myPackageExport.getPackage.getUsesConstraints.toArray.count(_.asInstanceOf[UsesConstraint].getPackageUsed.getName == "uses.that") == 1)
        assert(myPackageExport.getPackage.getUsesConstraints.toArray.count(_.asInstanceOf[UsesConstraint].getConstrainedPackage.getName == "my.package.name") == 2)

        assert(myOtherPackageExport.getPackage.getName == "my.other.package.name")
        assert(myOtherPackageExport.getVersion == "3.0.0")
        assert(myOtherPackageExport.getPackage.getUsesConstraints.size() == 2)
        assert(myOtherPackageExport.getPackage.getUsesConstraints.toArray.count(_.asInstanceOf[UsesConstraint].getPackageUsed.getName == "uses.this") == 1)
        assert(myOtherPackageExport.getPackage.getUsesConstraints.toArray.count(_.asInstanceOf[UsesConstraint].getConstrainedPackage.getName == "my.other.package.name") == 2)
        assert(myOtherPackageExport.getPackage.getUsesConstraints.toArray.count(_.asInstanceOf[UsesConstraint].getPackageUsed.getName == "uses.that") == 1)
        assert(myOtherPackageExport.getPackage.getUsesConstraints.toArray.count(_.asInstanceOf[UsesConstraint].getConstrainedPackage.getName == "my.other.package.name") == 2)

        assert(myThirdPackageExport.getPackage.getName == "my.third.package.name")
        assert(myThirdPackageExport.getVersion == "3.0.0")
        assert(myThirdPackageExport.getPackage.getUsesConstraints.size() == 2)
        assert(myThirdPackageExport.getPackage.getUsesConstraints.toArray.count(_.asInstanceOf[UsesConstraint].getPackageUsed.getName == "uses.this") == 1)
        assert(myThirdPackageExport.getPackage.getUsesConstraints.toArray.count(_.asInstanceOf[UsesConstraint].getConstrainedPackage.getName == "my.third.package.name") == 2)
        assert(myThirdPackageExport.getPackage.getUsesConstraints.toArray.count(_.asInstanceOf[UsesConstraint].getPackageUsed.getName == "uses.that") == 1)
        assert(myThirdPackageExport.getPackage.getUsesConstraints.toArray.count(_.asInstanceOf[UsesConstraint].getConstrainedPackage.getName == "my.third.package.name") == 2)

      }

    }

  }

}
