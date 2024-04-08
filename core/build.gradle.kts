publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            artifactId = "window-core"
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
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    api("net.bladehunt:ReaKt:0.1.0")
    api("net.kyori:adventure-api:4.15.0")
}

tasks.test {
    useJUnitPlatform()
}