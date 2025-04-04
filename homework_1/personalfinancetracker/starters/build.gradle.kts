plugins {
    id("java-library")
    id("io.spring.dependency-management") version "1.1.4"
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.spring.dependency-management")

    group = "website.ylab.learningplatform.starter"
    version = "1.0.0"

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    repositories {
        mavenCentral()
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework:spring-framework-bom:6.1.3")
        }
    }

    dependencies {
        implementation("org.springframework:spring-context")
        implementation("org.springframework:spring-aop")
        implementation("org.aspectj:aspectjrt:1.9.19")
        implementation("org.aspectj:aspectjweaver:1.9.19")
        implementation("org.slf4j:slf4j-api:2.0.9")

        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("org.mockito:mockito-core:5.16.0")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
