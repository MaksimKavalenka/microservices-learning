import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES

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

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom("org.apache.tika:tika-bom:2.9.0")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.4")
        mavenBom("software.amazon.awssdk:s3:2.22.13")
        mavenBom(BOM_COORDINATES)
    }

    dependencies {
        dependency("net.logstash.logback:logstash-logback-encoder:7.4")
        dependency("org.learning.microservices:resource-service-api:1.2.0")
        dependency("org.learning.microservices:song-service-api:1.2.0")
        dependency("org.learning.microservices:storage-service-api:1.0.0")
        dependency("org.learning.microservices:spring-microservices-starter:1.2.0")
    }
}

dependencies {
    implementation("io.github.openfeign:feign-micrometer")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("io.zipkin.reporter2:zipkin-reporter-brave")

    implementation("net.logstash.logback:logstash-logback-encoder")

    implementation("org.apache.tika:tika-core")
    implementation("org.apache.tika:tika-parsers-standard-package")

    implementation("org.learning.microservices:resource-service-api")
    implementation("org.learning.microservices:song-service-api")
    implementation("org.learning.microservices:storage-service-api")
    implementation("org.learning.microservices:spring-microservices-starter")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    implementation("org.slf4j:slf4j-api")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-rabbit")

    implementation("org.springframework.retry:spring-retry")

    implementation("software.amazon.awssdk:s3")
}

tasks.getByName<Jar>("jar") {
    enabled = false
    archiveClassifier.set("")
}
