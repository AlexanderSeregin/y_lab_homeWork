plugins {
    id("java")
    id("org.liquibase.gradle") version "2.2.0"
}

group = "website.ylab.learningplatform"
version = "1.0-SNAPSHOT"

// Configure the jar task to create a fat JAR with all dependencies
tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "website.ylab.learningplatform.Main"
        )
    }
    // Include all dependencies in the JAR
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    
    // Handle duplicate entries
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


repositories {
    mavenCentral()
}

dependencies {
    // DB dependencies
    implementation("org.postgresql:postgresql:42.7.1")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // Liquibase for migrations
    implementation("org.liquibase:liquibase-core:4.25.1")
    liquibaseRuntime("org.liquibase:liquibase-core:4.25.1")
    liquibaseRuntime("org.postgresql:postgresql:42.7.1")
    liquibaseRuntime("info.picocli:picocli:4.7.5")
    testImplementation("org.slf4j:slf4j-simple:2.0.17")
    implementation("org.slf4j:slf4j-api:2.0.17")

    // Config
    implementation("com.typesafe:config:1.4.3")
    implementation("org.apache.maven.plugins:maven-shade-plugin:3.6.0")

    // Testing
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.16.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.16.0")

    // Testcontainers
    testImplementation("org.testcontainers:testcontainers:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")
    testImplementation("org.testcontainers:postgresql:1.19.3")
}

tasks.test {
    useJUnitPlatform()
}
