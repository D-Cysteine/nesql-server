plugins {
    idea
    id("java")
    id("org.springframework.boot") version "3.0.2"
    id("io.spring.dependency-management") version "1.1.0"
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

group = "com.github.dcysteine.nesql.server"
version = "0.4.3"

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

    runtimeOnly("org.hsqldb:hsqldb:2.7.1")

    runtimeOnly("org.webjars:webjars-locator:0.46")
    runtimeOnly("org.webjars.npm:bootstrap:5.3.0-alpha1")
    runtimeOnly("org.webjars.npm:bootstrap-icons:1.10.3")
}

tasks.withType<Jar> {
    filesMatching("application.properties") {
        expand(
            mapOf("version" to project.version)
        )
    }
}