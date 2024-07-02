publishing {
    publications {
        create<MavenPublication>("library") {
            from(components["java"])
            artifactId = "window-minestom"
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

java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
}

repositories { maven("https://gitlab.com/api/v4/groups/bladehunt/-/packages/maven") }

dependencies {
    api(project(":core"))
    api("net.bladehunt:kotstom:0.2.0")
    compileOnly("net.minestom:minestom-snapshots:1c528d8ae2")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test { useJUnitPlatform() }
