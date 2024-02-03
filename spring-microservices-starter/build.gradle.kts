plugins {
    id("java")
    id("java-library")
    id("io.spring.dependency-management") version "1.1.3"
    id("maven-publish")
}

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

dependencies {
    api("org.flywaydb:flyway-core")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    api("org.springframework.boot:spring-boot-starter-data-jpa")
    api("org.springframework.boot:spring-boot-starter-web")

    api("software.amazon.awssdk:s3")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:3.1.4")
    }

    dependencies {
        dependency("software.amazon.awssdk:s3:2.22.13")
    }
}

tasks.getByName<Jar>("jar") {
    archiveClassifier.set("")
}
