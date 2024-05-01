import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22" apply false
    `maven-publish`
}

allprojects {
    group = "net.bladehunt"
    version = "0.1.0-alpha.4"

    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven("https://gitlab.com/api/v4/groups/bladehunt/-/packages/maven")
    }
    tasks.withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "21"
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