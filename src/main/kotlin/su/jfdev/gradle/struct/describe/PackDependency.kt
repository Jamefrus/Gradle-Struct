package su.jfdev.gradle.struct.describe

import org.gradle.api.artifacts.*
import org.gradle.api.internal.artifacts.*

data class PackDependency(val scope: Scope, val pack: Pack): Dependency, ResolvableDependency {

    override fun getName(): String = pack.project.name
    override fun getGroup() = pack.project.group.toString()
    override fun getVersion() = pack.project.version.toString()

    override fun copy() = PackDependency(scope, pack)

    override fun contentEquals(other: Dependency) = equals(other)

    override fun resolve(ctx: DependencyResolveContext) = pack.sourceSet.run {
        if(ctx.isTransitive) ctx += pack[scope]
        ctx += output
        ctx += resources
    }

    operator fun DependencyResolveContext.plusAssign(any: Any) = add(any)
}