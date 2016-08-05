package su.jfdev.gradle.service.describe

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.service.util.*

data class Pack(val project: Project, val name: String) {

    val sourceSet: SourceSet = project.sourceSets.maybeCreate(name)

    val configurations: Map<Scope, Configuration> = Scope.values().associate {
        it to it[project, name]
    }

    @JvmName("getAt")
    operator fun get(scope: Scope) = configurations[scope]!!

    infix fun extend(pack: Pack): Pack = eachScope {
        extend(pack, it)
    }

    infix fun depend(pack: Pack): Pack = eachScope {
        depend(pack, it)
    }

    private inline fun Pack.eachScope(block: (Scope) -> Unit) = apply {
        for (scope in Scope.values()) block(scope)
    }

    fun extend(pack: Pack, scope: Scope): Pack = apply {
        pack[scope] depend this[scope]
    }

    fun depend(pack: Pack, scope: Scope): Pack = apply {
        this[scope] depend pack[scope]
    }

    private infix fun Configuration.depend(configuration: Configuration) {
        extendsFrom(configuration)
    }

    companion object {
        @JvmStatic operator fun get(project: Project, name: String) = Pack(project, name)
    }
}