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
    api(project(":core"))
    compileOnly("net.minestom:minestom-snapshots:7320437640")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}