plugins {
    idea
    id("java")
    id("org.springframework.boot") version "3.0.1"
    id("io.spring.dependency-management") version "1.1.0"
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

group = "com.github.dcysteine.nesql.server"
version = "0.3.1"

repositories {
    mavenCentral()
}

dependencies {
    // Include SQL schema from NESQL Exporter.
    implementation(fileTree("libs") { include("NESQL-Exporter-*-sql.jar") })

    compileOnly("com.google.auto.value:auto-value-annotations:1.10.1")
    annotationProcessor("com.google.auto.value:auto-value:1.10.1")

    implementation("com.google.guava:guava:31.1-jre")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("com.h2database:h2:2.1.214")
}

tasks.withType<Jar> {
    filesMatching("application.properties") {
        expand(
            mapOf("version" to project.version)
        )
    }
}