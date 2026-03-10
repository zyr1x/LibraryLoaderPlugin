package ru.lewis.plugin.libraryloader.model

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.tasks.*

abstract class GenerateLibrariesTask : DefaultTask() {

    @get:Input
    abstract val repositories: MapProperty<String, String>

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

        sb.appendLine("[repositories]")
        repositories.get().forEach { (name, url) ->
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