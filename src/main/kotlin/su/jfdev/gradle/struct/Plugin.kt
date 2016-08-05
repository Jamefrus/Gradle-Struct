package su.jfdev.gradle.struct

import org.gradle.api.*
import org.gradle.api.Plugin
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.require.*

class Plugin: Plugin<Project> {
    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project
        project.plugins.apply("java")
        improveDescribe().improveImplementations()
        improveRequire()
    }

    private fun improveDescribe() = container("describe") {
        Pack(project, it)
    }.apply {
        maybeCreate("api") extend maybeCreate("main")
    }

    private fun NamedDomainObjectContainer<Pack>.improveImplementations() = container("implementations") {
        maybeCreate(it) depend maybeCreate("main") extend maybeCreate("test")
    }

    private fun container(name: String, factory: (String) -> Pack)
            = project.container(Pack::class.java, NamedDomainObjectFactory(factory)).apply {
        project.extensions.add(name, this)
    }

    private fun improveRequire() = project.extensions.create("require", RequireExtension::class.java, project)
}