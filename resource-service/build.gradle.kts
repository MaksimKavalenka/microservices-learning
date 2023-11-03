import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("java")
    id("org.springframework.boot") version "3.1.4"
}

apply(plugin = "io.spring.dependency-management")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.tika:tika-core")
    implementation("org.apache.tika:tika-parsers-standard-package")

    implementation("org.flywaydb:flyway-core")
    implementation("org.postgresql:postgresql")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    implementation("org.slf4j:slf4j-api")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation(files("libs/spring-microservices-starter-1.0.0.jar"))
    implementation(files("libs/song-service-api-1.0.0.jar"))
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom("org.apache.tika:tika-bom:2.9.0")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.4")
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
    archiveClassifier.set("")
}
