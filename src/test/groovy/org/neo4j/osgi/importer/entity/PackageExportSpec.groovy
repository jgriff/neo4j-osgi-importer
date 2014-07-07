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

    def "setting the bundle should implicitly hook the 'Bundle' to 'UsesConstraint#provider' for any/all of the UsesConstraints."() {
        Bundle bundle = new Bundle()

        // add some pre-existing packages
        Package fooPkg = new Package("com.foo")
        Package barPkg = new Package("com.bar")

        // a uses constraint
        UsesConstraint uc = new UsesConstraint().setPackagedUsed(barPkg)

        // the constraint exists on foo --> bar
        fooPkg.addUsesConstraint(uc)

        when:
        sut.setPackage(fooPkg)
        sut.setBundle(bundle)

        then:
        uc.getBundle() == bundle
    }

    def "setting the package should implicitly hook the 'Bundle' to 'UsesConstraint#provider' for any/all of the UsesConstraints in the package."() {
        Bundle bundle = new Bundle()

        // some new packages
        Package fooPkg = new Package("com.foo")
        Package barPkg = new Package("com.bar")

        // a uses constraint
        UsesConstraint uc = new UsesConstraint().setPackagedUsed(barPkg)

        // the constraint exists on foo --> bar
        fooPkg.addUsesConstraint(uc)

        when:
        sut.setBundle(bundle)
        sut.setPackage(fooPkg)

        then:
        uc.getBundle() == bundle
    }
}
