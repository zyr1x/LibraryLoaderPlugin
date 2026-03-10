plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "2.1.0"
}

group = "ru.lewis"
version = "1.0.0"

gradlePlugin {
    website = "https://github.com/zyr1x/LibraryLoaderPlugin"
    vcsUrl = "https://github.com/zyr1x/LibraryLoaderPlugin"

    plugins {
        register("libraryLoader") {
            id = "ru.lewis.plugin.libraryloader"
            implementationClass = "ru.lewis.plugin.libraryloader.LibraryLoaderPlugin"
            displayName = "Library Loader"
            description = "Generates libraries.toml into jar resources"
            tags = listOf("runtime", "dependencies", "loader")
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
}