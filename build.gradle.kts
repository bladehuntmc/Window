import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22" apply false
    `maven-publish`
}

allprojects {
    group = "net.bladehunt"
    version = "0.1.0-alpha"

    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://gitlab.com/api/v4/projects/54686082/packages/maven")
    }
    tasks.withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            compilerOptions {
                freeCompilerArgs.add("-Xcontext-receivers")
            }
        }
    }
}
subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")
}