package org.neo4j.osgi.importer.entity

import spock.lang.Specification

/**
 * @author <a href="mailto:justinrgriffin@gmail.com">Justin Griffin</a>
 * @since 0.0.1
 */
class PackageSpec extends Specification {
    Package sut

    def setup() {
        sut = new Package()
    }

    def "set name"() {
        when:
        sut.setName("foo")

        then:
        sut.getName() == "foo"
    }

    def "set name to null"() {
        when:
        sut.setName(null)

        then:
        sut.getName() == null
    }

    def "adding a UsesConstraint should implicitly set 'constrainedPackage' property"() {
        UsesConstraint uc = new UsesConstraint();

        when:
        sut.addUsesConstraint(uc)

        then:
        uc.getConstrainedPackage() == sut
    }
}
