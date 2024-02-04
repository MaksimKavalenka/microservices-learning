import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("java")
    id("java-library")
    id("maven-publish")
    id("org.springframework.boot") version "3.1.4" apply false
}

apply(plugin = "io.spring.dependency-management")

repositories {
    mavenCentral()
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
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.4")
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }

    dependencies {
        dependency("software.amazon.awssdk:s3:2.22.13")
    }
}

dependencies {
    api("org.apache.tomcat.embed:tomcat-embed-core")
    api("org.flywaydb:flyway-core")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    api("org.springframework:spring-web")
    api("org.springframework:spring-webmvc")
    api("org.springframework.boot:spring-boot")
    api("org.springframework.boot:spring-boot-autoconfigure")
    api("org.springframework.data:spring-data-jpa")

    api("software.amazon.awssdk:s3")
}

tasks.getByName<Jar>("jar") {
    archiveClassifier.set("")
}
