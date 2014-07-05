package org.neo4j.osgi.importer.entity

import spock.lang.Specification

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
class PackageExportSpec extends Specification {
    PackageExport sut

    def setup() {
        sut = new PackageExport()
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

    def "set/get version"() {
        when:
        sut.setVersion("1.0.0")

        then:
        sut.getVersion() == "1.0.0"
    }
}
