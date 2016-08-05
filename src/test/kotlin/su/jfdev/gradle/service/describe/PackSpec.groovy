package su.jfdev.gradle.service.describe

import org.gradle.api.Project
import su.jfdev.gradle.service.spec.ServiceSpec

import static su.jfdev.gradle.service.describe.Scope.COMPILE
import static su.jfdev.gradle.service.describe.Scope.RUNTIME

abstract class PackSpec extends ServiceSpec {

    Project getTarget() { project }

    Project getReceiver() { project }

    Pack pack

    def "should create configuration"() {
        expect:
        pack.sourceSet
        pack.name == "anySrc"
        pack.project == project

        when:
        def runtime = pack.configurations.get RUNTIME
        def compile = pack.configurations.get COMPILE

        then:
        runtime.name == "anySrcRuntime"
        compile.name == "anySrcCompile"
    }

    static class NonCreated extends PackSpec {
        void setup() {
            pack = Pack.get(project, "anySrc")
        }
    }

    static class Created extends PackSpec {
        void setup() {
            project.sourceSets.maybeCreate "anySrc"
            pack = Pack.get(project, "anySrc")
        }
    }
}