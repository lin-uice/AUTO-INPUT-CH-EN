plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
//    id("org.jetbrains.intellij") version
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

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        create("IC", "2025.1")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)

        // Add necessary plugin dependencies for compilation here, example:
        // bundledPlugin("com.intellij.java")
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
//patchPluginXml {
//    changeNotes """
//      <![CDATA[
//      插件开发学习功能点<br>
//      <em>1. 工程搭建</em>
//      <em>2. 菜单读取</em>
//      <em>3. 获取配置</em>
//      <em>4. 回显页面</em>
//    ]]>"""
//}
