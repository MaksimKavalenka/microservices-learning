import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("java")
    id("maven-publish")
    id("org.springframework.boot") version "3.1.4"
}

apply(plugin = "io.spring.dependency-management")
apply(plugin = "maven-publish")

configurations {
    listOf(apiElements, runtimeElements).forEach {
        val jar by tasks
        it.get().outgoing.artifacts.removeIf { it.buildDependencies.getDependencies(null).contains(jar) }
        it.get().outgoing.artifact(tasks.bootJar)
    }
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

dependencies {
    implementation("org.apache.tika:tika-core")
    implementation("org.apache.tika:tika-parsers-standard-package")

    implementation("org.learning.microservices:resource-service-api")
    implementation("org.learning.microservices:song-service-api")
    implementation("org.learning.microservices:spring-microservices-starter")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    implementation("org.slf4j:slf4j-api")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.retry:spring-retry")

    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-rabbit")

    implementation("software.amazon.awssdk:s3")
}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom("org.apache.tika:tika-bom:2.9.0")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.4")
        mavenBom("software.amazon.awssdk:s3:2.22.13")
    }

    dependencies {
        dependency("org.learning.microservices:resource-service-api:1.1.0")
        dependency("org.learning.microservices:song-service-api:1.2.0")
        dependency("org.learning.microservices:spring-microservices-starter:1.1.0")
    }
}

tasks.getByName<Jar>("jar") {
    enabled = false
    archiveClassifier.set("")
}
