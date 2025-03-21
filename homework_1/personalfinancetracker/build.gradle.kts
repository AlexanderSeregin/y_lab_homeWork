plugins {
    id("java")
    id("org.liquibase.gradle") version "2.2.0"
    application
}

group = "website.ylab.learningplatform"
version = "1.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("website.ylab.learningplatform.Main")
    // Remove AspectJ agent configuration as we'll use a different approach
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "website.ylab.learningplatform.Main"
        )
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    
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
    implementation("org.slf4j:slf4j-simple:2.0.17")

    // Config
    implementation("com.typesafe:config:1.4.3")
    implementation("org.apache.maven.plugins:maven-shade-plugin:3.6.0")
    
    // Use Jetty 9.x which is compatible with javax.servlet
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    implementation("org.eclipse.jetty:jetty-server:9.4.51.v20230217")
    implementation("org.eclipse.jetty:jetty-servlet:9.4.51.v20230217")
    
    // Jackson for JSON processing
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")
    
    // Validation
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.hibernate.validator:hibernate-validator:6.2.5.Final") // Changed to be compatible with javax
    implementation("org.glassfish:javax.el:3.0.0")
    
    // MapStruct for DTO mapping
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    
    // AspectJ for AOP
    implementation("org.aspectj:aspectjrt:1.9.21")
    implementation("org.aspectj:aspectjweaver:1.9.21")

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
    useJUnitPlatform {
        includeEngines("junit-jupiter", "junit-vintage")
        systemProperty("junit.jupiter.execution.parallel.enabled", "false")
        systemProperty("junit.jupiter.execution.parallel.mode.default", "sequential")
        systemProperty("junit.jupiter.execution.parallel.mode.classes.default", "sequential")

    }
}
