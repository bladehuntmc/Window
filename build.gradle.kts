import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.22" apply false
}

allprojects {
    group = "net.bladehunt"
    version = "0.0.1-ALPHA"

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
}