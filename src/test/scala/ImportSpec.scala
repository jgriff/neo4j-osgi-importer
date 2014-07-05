
import org.neo4j.osgi.parser.Import
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * @author <a href="mailto:brdlmaier49@gmail.com">Bradley Maier</a>
 * @since 0.0.1
 */

@RunWith(classOf[JUnitRunner])
class ImportSpec extends FunSpec {
  describe("Import") {
    describe("Should parse a single package import statement") {
      it("Should produce an ImportPackage") {
        assert(Import.parse("my.package.name").length == 1)
      }

      it("Should have have the correct name, version, and resolution") {
        val importPackage = Import.parse("my.package.name")(0)
        assert(importPackage.getPackage.getName == "my.package.name")
        assert(importPackage.getMinVersion == "0.0.0")
        assert(importPackage.getMaxVersion == null)
        assert(importPackage.isMinVersionInclusive)
        assert(importPackage.isMandatory)
        assert(!importPackage.isMaxVersionInclusive)
      }

      it("Should correctly parse a version attribute") {
        val importPackage = Import.parse("my.package.name;version=1.0.0")(0)
        assert(importPackage.getPackage.getName == "my.package.name")
        assert(importPackage.getMinVersion == "1.0.0")
        assert(importPackage.getMaxVersion == null)
        assert(importPackage.isMinVersionInclusive)
        assert(importPackage.isMandatory)
        assert(!importPackage.isMaxVersionInclusive)
      }

      it("Should correctly parse an inclusive version range") {
        val importPackage = Import.parse("my.package.name;version=\"[1.0.0,2.0.0]\"")(0)
        assert(importPackage.getPackage.getName == "my.package.name")
        assert(importPackage.getMinVersion == "1.0.0")
        assert(importPackage.getMaxVersion == "2.0.0")
        assert(importPackage.isMinVersionInclusive)
        assert(importPackage.isMandatory)
        assert(importPackage.isMaxVersionInclusive)
      }

      it("Should correctly parse an exclusive version range") {
        val importPackage = Import.parse("my.package.name;version=\"(1.0.0,2.0.0)\"")(0)
        assert(importPackage.getPackage.getName == "my.package.name")
        assert(importPackage.getMinVersion == "1.0.0")
        assert(importPackage.getMaxVersion == "2.0.0")
        assert(!importPackage.isMinVersionInclusive)
        assert(importPackage.isMandatory)
        assert(!importPackage.isMaxVersionInclusive)
      }

      it("Should correctly parse a resolution") {
        val importPackage = Import.parse("my.package.name;version=\"(1.0.0,2.0.0)\";resolution:=bananas")(0)
        assert(importPackage.getPackage.getName == "my.package.name")
        assert(importPackage.getMinVersion == "1.0.0")
        assert(importPackage.getMaxVersion == "2.0.0")
        assert(!importPackage.isMinVersionInclusive)
        assert(!importPackage.isMandatory)
        assert(!importPackage.isMaxVersionInclusive)
      }

      it("Should handle multiple package names") {
        val importPackages = Import.parse(
          "my.package.name;my.other.package.name;my.third.package.name;version=\"(1.0.0,2.0.0)\";resolution:=bananas"
        )
        assert(importPackages.length == 3)
        val myPackage = importPackages(0)
        val myOtherPackage = importPackages(1)
        val myThirdPackage = importPackages(2)

        assert(myPackage.getPackage.getName == "my.package.name")
        assert(myPackage.getMinVersion == "1.0.0")
        assert(myPackage.getMaxVersion == "2.0.0")
        assert(!myPackage.isMinVersionInclusive)
        assert(!myPackage.isMandatory)
        assert(!myPackage.isMaxVersionInclusive)

        assert(myOtherPackage.getPackage.getName == "my.other.package.name")
        assert(myOtherPackage.getMinVersion == "1.0.0")
        assert(myOtherPackage.getMaxVersion == "2.0.0")
        assert(!myOtherPackage.isMinVersionInclusive)
        assert(!myOtherPackage.isMandatory)
        assert(!myOtherPackage.isMaxVersionInclusive)

        assert(myThirdPackage.getPackage.getName == "my.third.package.name")
        assert(myThirdPackage.getMinVersion == "1.0.0")
        assert(myThirdPackage.getMaxVersion == "2.0.0")
        assert(!myThirdPackage.isMinVersionInclusive)
        assert(!myThirdPackage.isMandatory)
        assert(!myThirdPackage.isMaxVersionInclusive)
      }

    }

  }

}
