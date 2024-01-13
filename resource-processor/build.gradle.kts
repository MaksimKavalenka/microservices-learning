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

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    implementation("org.slf4j:slf4j-api")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation(files("libs/song-service-api-1.1.0.jar"))
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom("org.apache.tika:tika-bom:2.9.0")
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
    archiveClassifier.set("")
}
