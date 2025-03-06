plugins {
    id("java")
}

group = "website.ylab.learningplatform"
version = "1.0-SNAPSHOT"

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "website.ylab.learningplatform.Main"
        )
    }
}


repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

