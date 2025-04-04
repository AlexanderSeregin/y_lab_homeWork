plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.liquibase.gradle") version "2.2.0"
    application
}

group = "website.ylab.learningplatform"
version = "5.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("website.ylab.learningplatform.Main")
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "website.ylab.learningplatform.Main"
        )
    }
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // Explicitly declare dependencies on the starter modules
    dependsOn(":starters:logging-starter:jar", ":starters:audit-starter:jar")
}


repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // Custom starters
    implementation(project(":starters:logging-starter"))
    implementation(project(":starters:audit-starter"))

    // Swagger dependencies
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    // DB dependencies
    implementation("org.postgresql:postgresql:42.7.2")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // Liquibase for migrations
    implementation("org.liquibase:liquibase-core:4.25.1")
    liquibaseRuntime("org.liquibase:liquibase-core:4.25.1")
    liquibaseRuntime("org.postgresql:postgresql:42.7.2")
    liquibaseRuntime("info.picocli:picocli:4.7.5")

    // Config
    implementation("com.typesafe:config:1.4.3")
    implementation("org.apache.maven.plugins:maven-shade-plugin:3.6.0")

    // Jackson for JSON processing
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.16.1")

    // MapStruct for DTO mapping
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    // Validation
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.hibernate.validator:hibernate-validator:6.2.5.Final")
    implementation("org.glassfish:javax.el:3.0.0")

    // Logging
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.16.1")
    implementation("org.aspectj:aspectjrt:1.9.19")
    implementation("org.aspectj:aspectjweaver:1.9.19")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.16.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.16.0")
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
