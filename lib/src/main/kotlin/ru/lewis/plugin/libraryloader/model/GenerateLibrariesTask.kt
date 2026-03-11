package ru.lewis.plugin.libraryloader.model

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.*

abstract class GenerateLibrariesTask : DefaultTask() {

    @get:Input
    abstract val libraries: ListProperty<String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val output = outputDir.get().asFile
        output.mkdirs()

        val file = output.resolve("libraries.toml")
        val sb = StringBuilder()

        val repos = project.repositories
            .filterIsInstance<MavenArtifactRepository>()
            .associate { repo ->
                val name = repo.name
                    .lowercase()
                    .replace(" ", "_")
                    .replace("-", "_")
                val url = repo.url.toString().trimEnd('/')
                name to url
            }

        sb.appendLine("[repositories]")
        if (repos.none { it.value.contains("repo1.maven.org") }) {
            sb.appendLine("""central = "https://repo1.maven.org/maven2"""")
        }
        repos.forEach { (name, url) ->
            sb.appendLine("""$name = "$url"""")
        }

        sb.appendLine()

        sb.appendLine("[libraries]")
        libraries.get().forEach { notation ->
            val alias = notation
                .substringAfter(":")
                .substringBefore(":")
                .replace("-", "_")
            sb.appendLine("""$alias = "$notation"""")
        }

        file.writeText(sb.toString())
        logger.lifecycle("Generated ${file.path}")
    }
}