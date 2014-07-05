package org.neo4j.osgi.importer.entity

import spock.lang.Specification

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
class PackageImportSpec extends Specification {
    PackageImport sut

    def setup() {
        sut = new PackageImport()
    }

    def "set/get bundle"() {
        Bundle bundle = new Bundle()

        when:
        sut.setBundle(bundle)

        then:
        sut.getBundle() == bundle
    }

    def "set/get package"() {
        Package pkg = new Package()

        when:
        sut.setPackage(pkg)

        then:
        sut.getPackage() == pkg
    }

    def "set/get minimum version"() {
        when:
        sut.setMinVersion("1.0.0")

        then:
        sut.getMinVersion() == "1.0.0"
    }

    def "minimum version is inclusive by default"() {
        expect:
        sut.isMinVersionInclusive()
    }

    def "set/get minimum version inclusive"() {
        when:
        sut.setMinVersionInclusive(true)

        then:
        sut.isMinVersionInclusive()
    }

    def "set/get minimum version exclusive"() {
        when:
        sut.setMinVersionInclusive(false)

        then:
        !sut.isMinVersionInclusive()
    }

    def "maximum version is exclusive by default"() {
        expect:
        !sut.isMaxVersionInclusive()
    }

    def "set/get maximum version inclusive"() {
        when:
        sut.setMaxVersionInclusive(true)

        then:
        sut.isMaxVersionInclusive()
    }

    def "set/get maximum version exclusive"() {
        when:
        sut.setMaxVersionInclusive(false)

        then:
        !sut.isMaxVersionInclusive()
    }

    def "import is 'mandatory' by default"() {
        expect:
        sut.isMandatory()
    }

    def "set package import to 'mandatory'"() {
        when:
        sut.setMandatory(true)

        then:
        sut.isMandatory()
    }

    def "set package import to not 'mandatory'"() {
        when:
        sut.setMandatory(false)

        then:
        !sut.isMandatory()
    }
}
