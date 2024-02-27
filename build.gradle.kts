import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    `maven-publish`
    `kotlin-dsl`
    `always-up-to-date`
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.paperweight)
}

kotlin.jvmToolchain {
    languageVersion = JavaLanguageVersion.of(17)
}

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/") {
        content {
            onlyForConfigurations(configurations.paperclip.name)
        }
    }
}

dependencies {
    remapper(libs.remapper)
    decompiler(libs.decompiler)
    paperclip(libs.paperclip)
}

allprojects {
    apply(plugin = "java")
    apply(plugin = "maven-publish")

    java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

    publishing {
        repositories {
            maven {
                name = "githubPackage"
                url = uri("https://maven.pkg.github.com/PlazmaMC/PlazmaBukkit")

                credentials {
                    username = System.getenv("GITHUB_USERNAME")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }

            publications.register<MavenPublication>("gpr") {
                from(components["java"])
            }
        }
    }
}

subprojects {
    tasks {
        withType<JavaCompile>().configureEach {
            options.compilerArgs.addAll(listOf("--add-modules=jdk.incubator.vector", "-Xmaxwarns", "1"))
            options.encoding = Charsets.UTF_8.name()
            options.release = 17
        }
    
        withType<Javadoc> {
            options.encoding = Charsets.UTF_8.name()
        }
    
        withType<ProcessResources> {
            filteringCharset = Charsets.UTF_8.name()
        }
    
        withType<Test> {
            testLogging {
                showStackTraces = true
                exceptionFormat = TestExceptionFormat.FULL
                events(TestLogEvent.STANDARD_OUT)
            }
        }
    }

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://papermc.io/repo/repository/maven-public/")
    }
}

paperweight {
    serverProject = project(":plazma-server")

    remapRepo = "https://repo.papermc.io/repository/maven-public/"
    decompileRepo = "https://repo.papermc.io/repository/maven-public/"

    usePaperUpstream(providers.gradleProperty("paperCommit")) {
        withPaperPatcher {
            apiPatchDir.set(layout.projectDirectory.dir("patches/api"))
            apiOutputDir.set(layout.projectDirectory.dir("Plazma-API"))

            serverPatchDir.set(layout.projectDirectory.dir("patches/server"))
            serverOutputDir.set(layout.projectDirectory.dir("Plazma-Server"))
        }

        patchTasks.register("generatedApi") {
            isBareDirectory = true
            upstreamDirPath = "paper-api-generator/generated"
            patchDir = layout.projectDirectory.dir("patches/generated-api")
            outputDir = layout.projectDirectory.dir("paper-api-generator/generated")
        }

        patchTasks.register("mojangApi") {
            isBareDirectory = true
            upstreamDirPath = "Paper-MojangAPI"
            patchDir = layout.projectDirectory.dir("patches/mojang-api")
            outputDir = layout.projectDirectory.dir("Plazma-MojangAPI")
        }
    }
}

alwaysUpToDate {
    paperRepository.set("https://github.com/PaperMC/Paper")
    paperBranch.set("master")
    purpurRepository.set("https://github.com/PurpurMC/Purpur")
    purpurBranch.set("ver/1.20.4")
    pufferfishRepository.set("https://github.com/pufferfish-gg/Pufferfish")
    pufferfishBranch.set("ver/1.20")
}

tasks {
    applyPatches {
        dependsOn("applyGeneratedApiPatches")
    }

    rebuildPatches {
        dependsOn("rebuildGeneratedApiPatches")
    }

    generateDevelopmentBundle {
        apiCoordinates.set("org.plazmamc.plazma:plazma-api")
        mojangApiCoordinates.set("io.papermc.paper:paper-mojangapi")
        libraryRepositories.addAll(
                "https://repo.maven.apache.org/maven2/",
                "https://maven.pkg.github.com/PlazmaMC/Plazma",
                "https://papermc.io/repo/repository/maven-public/"
        )
    }
}

publishing {
    publications.create<MavenPublication>("devBundle") {
        artifact(tasks.generateDevelopmentBundle) {
            artifactId = "dev-bundle"
        }
    }
}
