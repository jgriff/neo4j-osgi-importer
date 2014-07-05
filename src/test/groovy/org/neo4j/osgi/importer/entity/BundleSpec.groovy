package org.neo4j.osgi.importer.entity

import spock.lang.Specification

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
class BundleSpec extends Specification {
    Bundle sut

    def setup() {
        sut = new Bundle()
    }

    def "default Bundle-SymbolicName"() {
        expect:
        sut.getBundleSymbolicName() == null
    }

    def "set/get Bundle-SymbolicName"() {
        when:
        sut.setBundleSymbolicName("foo.bar")

        then:
        sut.getBundleSymbolicName() == "foo.bar"
    }

    def "set/get Bundle-SymbolicName to null"() {
        when:
        sut.setBundleSymbolicName(null)

        then:
        sut.getBundleSymbolicName() == null
    }

    def "default Version"() {
        expect:
        sut.getVersion() == null
    }

    def "set/get Version"() {
        when:
        sut.setVersion("1.0.0")

        then:
        sut.getVersion() == "1.0.0"
    }

    def "set/get Version to null"() {
        when:
        sut.setVersion()

        then:
        sut.getVersion() == null
    }

    def "default Package-Import"() {
        expect:
        sut.getPackageImports() == null
    }

    def "add Package-Import"() {
        PackageImport pi = new PackageImport()

        when:
        sut.addPackageImport(pi)

        then:
        sut.getPackageImports().size() == 1
        sut.getPackageImports().iterator().next() == pi
    }

    def "add duplicate Package-Import"() {
        PackageImport pi = new PackageImport()

        when:
        sut.addPackageImport(pi)
        sut.addPackageImport(pi)

        then:
        sut.getPackageImports().size() == 1
        sut.getPackageImports().iterator().next() == pi
    }

    def "add null Package-Import"() {
        when:
        sut.addPackageImport(null)

        then:
        sut.getPackageImports().size() == 0
    }

    def "default Package-Export"() {
        expect:
        sut.getPackageExports() == null
    }

    def "add Package-Export"() {
        PackageExport pe = new PackageExport()

        when:
        sut.addPackageExport(pe)

        then:
        sut.getPackageExports().size() == 1
        sut.getPackageExports().iterator().next() == pe
    }

    def "add duplicate Package-Export"() {
        PackageExport pe = new PackageExport()

        when:
        sut.addPackageExport(pe)
        sut.addPackageExport(pe)

        then:
        sut.getPackageExports().size() == 1
        sut.getPackageExports().iterator().next() == pe
    }

    def "add null Package-Export"() {
        when:
        sut.addPackageExport(null)

        then:
        sut.getPackageExports().size() == 0
    }
}
