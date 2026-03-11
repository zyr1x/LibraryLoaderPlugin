package ru.lewis.plugin.libraryloader.model

abstract class LibraryLoaderExtension {
    internal val libraries = mutableListOf<String>()

    fun library(notation: String) {
        require(notation.split(":").size == 3) {
            "Invalid notation: $notation — expected group:artifact:version"
        }
        libraries.add(notation)
    }
}