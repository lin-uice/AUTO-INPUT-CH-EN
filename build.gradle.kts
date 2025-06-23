import nl.adaptivity.xmlutil.util.impl.createDocument
import org.gradle.internal.declarativedsl.dom.resolution.documentWithResolution

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.5.0"
}

group = "cn.mumukehao"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

val platformPlugins: String by project
// Configure Gradle IntelliJ Plugin
dependencies {
    implementation("com.maddyhome.idea:ideavim:2.23.0")

    intellijPlatform {
        create("IC", "2025.1")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
        bundledPlugins(providers.gradleProperty("platformBundledPlugins").map { it.split(',') })
        plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })
//        plugins(providers.gradleProperty("platformPlugins").map { it.split(',') })
//        bundledPlugin("IdeaVIM")
    }
}
idea{
    module{
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}
intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "251"
        }

        changeNotes = """
      Initial version
    """.trimIndent()
    }
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "21"
    }
}

