import java.util.Locale

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

if (!file(".git").exists()) {
    val errorText = """
        
        =====================[ ERROR ]=====================
         The Plazma project directory is not a properly cloned Git repository.
         
         In order to build Plazma from source you must clone
         the Plazma repository using Git, not download a code
         zip from GitHub.
         
         Built Plazma jars are available for download at
         https://plazmamc.org/downloads
         
         See https://github.com/PlazmaMC/Plazma/blob/HEAD/CONTRIBUTING.md
         for further information on building and modifying Plazma.
        ===================================================
    """.trimIndent()
    error(errorText)
}

rootProject.name = "plazma"
for (name in listOf("plazma-api", "plazma-server")) {
    val projName = name.lowercase(Locale.ENGLISH)
    include(projName)
    findProject(":$projName")!!.projectDir = file(name)
}

optionalInclude("test-plugin")

fun optionalInclude(name: String, op: (ProjectDescriptor.() -> Unit)? = null) {
    val settingsFile = file("$name.settings.gradle.kts")
    if (settingsFile.exists()) {
        apply(from = settingsFile)
        findProject(":$name")?.let { op?.invoke(it) }
    } else {
        settingsFile.writeText(
            """
            // Uncomment to enable the '$name' project
            // include(":$name")

            """.trimIndent()
        )
    }
}
