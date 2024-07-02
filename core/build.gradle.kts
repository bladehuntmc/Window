publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            artifactId = "window-core"
        }
    }
    repositories {
        maven {
            name = "releases"
            url = uri("https://mvn.bladehunt.net/releases")
            credentials(PasswordCredentials::class) {
                username = System.getenv("MAVEN_NAME")
                password = System.getenv("MAVEN_SECRET")
            }
            authentication { create<BasicAuthentication>("basic") }
        }
    }
}

java { withSourcesJar() }

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    api("net.bladehunt:reakt:0.1.1")
    compileOnly("net.kyori:adventure-api:4.15.0")
}

tasks.test { useJUnitPlatform() }
