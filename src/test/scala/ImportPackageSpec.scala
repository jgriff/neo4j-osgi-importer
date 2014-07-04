import org.neo4j.osgi.parser.ImportPackage
import org.scalatest.FunSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * @author <a href="mailto:brdlmaier49@gmail.com">Bradley Maier</a>
 * @since 0.0.1
 */
@RunWith(classOf[JUnitRunner])
class ImportPackageSpec extends FunSpec {

  describe("ImportPackage") {
    it("Should handle a single import statement") {
      assert(ImportPackage.parse("my.package.name;version=\"(1.0.0,2.0.0)\";resolution:=bananas").size() == 1)
    }

    it("Should handle multiple import statements") {
      val imports = ImportPackage.parse("my.package.name;my.other.package.name;version=\"(1.0.0,2.0.0)\";resolution:=bananas" +
        ",my.third.package.name;version=\"3.0.0,5.0.0]\";resolution:=mandatory")
      assert(imports.size() == 3)

      val myPackage = imports.get(0)
      val myOtherPackage = imports.get(1)
      val myThirdPackage = imports.get(2)

      assert(myPackage.getPakage.getName == "my.package.name")
      assert(myPackage.getMinVersion == "1.0.0")
      assert(myPackage.getMaxVersion == "2.0.0")
      assert(!myPackage.isMinVersionInclusive)
      assert(!myPackage.isMandatory)
      assert(!myPackage.isMaxVersionInclusive)

      assert(myOtherPackage.getPakage.getName == "my.other.package.name")
      assert(myOtherPackage.getMinVersion == "1.0.0")
      assert(myOtherPackage.getMaxVersion == "2.0.0")
      assert(!myOtherPackage.isMinVersionInclusive)
      assert(!myOtherPackage.isMandatory)
      assert(!myOtherPackage.isMaxVersionInclusive)

      assert(myThirdPackage.getPakage.getName == "my.third.package.name")
      assert(myThirdPackage.getMinVersion == "3.0.0")
      assert(myThirdPackage.getMaxVersion == "5.0.0")
      assert(myThirdPackage.isMinVersionInclusive)
      assert(myThirdPackage.isMandatory)
      assert(myThirdPackage.isMaxVersionInclusive)
    }
  }
}
