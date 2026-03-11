package ru.lewis.plugin.libraryloader

import org.gradle.api.Plugin
import org.gradle.api.Project
import ru.lewis.plugin.libraryloader.model.GenerateLibrariesTask
import ru.lewis.plugin.libraryloader.model.LibraryLoaderExtension

class LibraryLoaderPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions
            .create("libraryLoader", LibraryLoaderExtension::class.java)

        project.afterEvaluate {
            extension.libraries.forEach { notation ->
                project.dependencies.add("compileOnly", notation)
            }
        }

        val generateTask = project.tasks.register(
            "generateLibraries",
            GenerateLibrariesTask::class.java
        ) { task ->
            task.group = "library-loader"
            task.description = "Generates libraries.toml into resources"

            task.libraries.set(
                project.provider { extension.libraries }
            )
            task.outputDir.set(
                project.layout.buildDirectory.dir("generated/library-loader")
            )
        }

        project.plugins.withId("java") {
            val sourceSets = project.extensions
                .getByType(org.gradle.api.tasks.SourceSetContainer::class.java)

            sourceSets.named("main") { sourceSet ->
                sourceSet.resources.srcDir(
                    generateTask.map { it.outputDir }
                )
            }
        }

        project.tasks.named("processResources") {
            it.dependsOn(generateTask)
        }
    }
}