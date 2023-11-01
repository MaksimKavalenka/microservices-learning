import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("java")
    id("java-library")
    id("org.springframework.boot") version "3.1.4"
}

allprojects {
    apply {
        plugin("java")
        plugin("java-library")
        plugin("io.spring.dependency-management")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        annotationProcessor("org.projectlombok:lombok")
        compileOnly("org.projectlombok:lombok")
    }

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.4")
        }

        dependencies {
            dependencySet("org.mapstruct:1.5.5.Final") {
                entry("mapstruct")
                entry("mapstruct-processor")
            }
        }
    }

    tasks.getByName<Jar>("jar") {
        archiveClassifier.set("")
    }
}

dependencies {
    implementation("org.flywaydb:flyway-core")

    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    implementation("org.postgresql:postgresql")

    implementation("org.slf4j:slf4j-api")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    implementation(project(":song-service-api"))
    implementation(files("libs/spring-microservices-starter-1.0.0.jar"))
}

tasks.getByName<Jar>("jar") {
    enabled = false
    archiveClassifier.set("")
}
