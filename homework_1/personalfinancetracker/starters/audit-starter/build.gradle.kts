plugins {
    id("java-library")
    id("maven-publish")
}

dependencies {
    // Explicitly include Spring dependencies
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-aop")
    implementation("org.aspectj:aspectjrt:1.9.19")
    implementation("org.aspectj:aspectjweaver:1.9.19")
    implementation("org.slf4j:slf4j-api:2.0.9")

    api(project(":starters:logging-starter"))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
