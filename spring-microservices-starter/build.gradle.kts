plugins {
    id("java")
    id("java-library")
    id("io.spring.dependency-management") version "1.1.3"
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.flywaydb:flyway-core")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-web")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.1.4")
    }
}

tasks.getByName<Jar>("jar") {
    archiveClassifier.set("")
}
