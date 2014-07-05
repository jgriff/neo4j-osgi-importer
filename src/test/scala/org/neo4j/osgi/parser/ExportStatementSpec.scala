package org.neo4j.osgi.parser


import org.junit.runner.RunWith
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner

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
        val myPackage = exportPackages(0)
        val myOtherPackage = exportPackages(1)
        val myThirdPackage = exportPackages(2)

        assert(myPackage.getPackage.getName == "my.package.name")
        assert(myPackage.getVersion == "3.0.0")

        assert(myOtherPackage.getPackage.getName == "my.other.package.name")
        assert(myOtherPackage.getVersion == "3.0.0")

        assert(myThirdPackage.getPackage.getName == "my.third.package.name")
        assert(myThirdPackage.getVersion == "3.0.0")
      }

    }

  }

}
