import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

plugins {
    id("java")
    id("java-library")
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

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.4")
        mavenBom(BOM_COORDINATES)
    }
}

dependencies {
    implementation("io.micrometer:micrometer-registry-prometheus")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    implementation("org.slf4j:slf4j-api")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
}

tasks.getByName<Jar>("jar") {
    enabled = false
    archiveClassifier.set("")
}
