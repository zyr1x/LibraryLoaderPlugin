package ru.lewis.plugin.libraryloader.model

abstract class LibraryLoaderExtension {
    internal val repositories = mutableMapOf(
        "central" to "https://repo1.maven.org/maven2"  // ← всегда есть
    )
    internal val libraries = mutableListOf<String>()

    fun repository(name: String, url: String) {
        repositories[name] = url
    }

    fun library(notation: String) {
        require(notation.split(":").size == 3) {
            "Invalid notation: $notation — expected group:artifact:version"
        }
        libraries.add(notation)
    }
}