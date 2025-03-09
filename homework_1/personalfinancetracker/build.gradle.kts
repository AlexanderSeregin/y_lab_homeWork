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
    testImplementation("org.mockito:mockito-core:5.16.0")
    //testImplementation("org.junit.jupiter:junit-jupiter-api:5.12.0")
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
    //testImplementation("org.junit.jupiter:junit-jupiter-engine:5.12.0")
    // https://mvnrepository.com/artifact/org.mockito/mockito-junit-jupiter
    testImplementation("org.mockito:mockito-junit-jupiter:5.16.0")
}

tasks.test {
    useJUnitPlatform()
}

