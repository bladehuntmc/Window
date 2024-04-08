publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            artifactId = "window-minestom"
        }
    }
    repositories {
        maven {
            url = uri("https://gitlab.com/api/v4/projects/54684809/packages/maven")
            credentials(HttpHeaderCredentials::class) {
                name = "Job-Token"
                value = properties["CI_JOB_TOKEN"] as String?
            }
            authentication {
                create("header", HttpHeaderAuthentication::class)
            }
        }
    }
}

dependencies {
    api("net.minestom:minestom-snapshots:7320437640")
    //api("net.bladehunt:ReaKt:8b0ade6")
    api(project(":core"))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}