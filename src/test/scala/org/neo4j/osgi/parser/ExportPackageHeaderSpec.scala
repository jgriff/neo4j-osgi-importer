package org.neo4j.osgi.parser

import org.scalatest.FunSpec
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * @author <a href="mailto:brdlmaier49@gmail.com">Bradley Maier</a>
 * @since 0.0.1
 */

@RunWith(classOf[JUnitRunner])
class ExportPackageHeaderSpec extends FunSpec {
  describe("ExportPackageHeader") {

    it("Should handle null and empty strings") {
      assert(ExportPackageHeader.parse(null).size() == 0)
      assert(ExportPackageHeader.parse("").size() == 0)
    }

    it("Should handle a single export statement") {
      assert(ExportPackageHeader.parse("my.package.name;version=1.0.0;resolution:=bananas").size() == 1)
    }

    it("Should handle multiple export statements") {
      val exports = ExportPackageHeader.parse("my.package.name;my.other.package.name;version=1.0.0;resolution:=bananas" +
        ",my.third.package.name;version=3.0.0;resolution:=mandatory")
      assert(exports.size() == 3)

      val myPackage = exports.get(0)
      val myOtherPackage = exports.get(1)
      val myThirdPackage = exports.get(2)

      assert(myPackage.getPackage.getName == "my.package.name")
      assert(myPackage.getVersion == "1.0.0")

      assert(myOtherPackage.getPackage.getName == "my.other.package.name")
      assert(myOtherPackage.getVersion == "1.0.0")

      assert(myThirdPackage.getPackage.getName == "my.third.package.name")
      assert(myThirdPackage.getVersion == "3.0.0")
    }
  }

}
