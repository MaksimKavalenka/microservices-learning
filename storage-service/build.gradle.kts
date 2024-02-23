import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("org.springframework.boot") version "3.1.4"
}

configurations {
    listOf(apiElements, runtimeElements).forEach {
        val jar by tasks
        it.get().outgoing.artifacts.removeIf { it.buildDependencies.getDependencies(null).contains(jar) }
        it.get().outgoing.artifact(tasks.bootJar)
    }
}

allprojects {
    apply {
        plugin("java")
        plugin("java-library")
        plugin("io.spring.dependency-management")
        plugin("maven-publish")
    }

    repositories {
        mavenCentral()

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/MaksimKavalenka/microservices-learning")
            credentials {
                username = project.findProperty("gpr.user") as String
                password = project.findProperty("gpr.key") as String
            }
        }
    }

    configure<PublishingExtension> {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/MaksimKavalenka/microservices-learning")
                credentials {
                    username = project.findProperty("gpr.user") as String
                    password = project.findProperty("gpr.key") as String
                }
            }
        }

        publications {
            register<MavenPublication>("gpr") {
                from(components["java"])
                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }
            }
        }
    }

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom(BOM_COORDINATES)
        }
    }

    dependencies {
        annotationProcessor("org.projectlombok:lombok")
        compileOnly("org.projectlombok:lombok")
    }

    tasks.getByName<Jar>("jar") {
        archiveClassifier.set("")
    }
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.4")
        mavenBom(BOM_COORDINATES)
    }

    dependencies {
        dependencySet("org.mapstruct:1.5.5.Final") {
            entry("mapstruct")
            entry("mapstruct-processor")
        }

        dependency("net.logstash.logback:logstash-logback-encoder:7.4")
        dependency("org.learning.microservices:spring-microservices-starter:1.2.0")
        dependency("software.amazon.awssdk:s3:2.22.13")
    }
}

dependencies {
    implementation(project(":storage-service-api"))

    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("net.logstash.logback:logstash-logback-encoder")
    implementation("org.flywaydb:flyway-core")
    implementation("org.learning.microservices:spring-microservices-starter")

    implementation("org.mapstruct:mapstruct")
    annotationProcessor("org.mapstruct:mapstruct-processor")

    implementation("org.postgresql:postgresql")
    implementation("org.slf4j:slf4j-api")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.retry:spring-retry")

    implementation("software.amazon.awssdk:s3")
}

tasks.getByName<Jar>("jar") {
    enabled = false
    archiveClassifier.set("")
}
