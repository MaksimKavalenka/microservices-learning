import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("groovy")
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
        plugin("groovy")
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

    dependencies {
        annotationProcessor("org.projectlombok:lombok")
        compileOnly("org.projectlombok:lombok")
    }

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.4")
            mavenBom("org.spockframework:spock-bom:2.4-M1-groovy-4.0")
            mavenBom("org.testcontainers:testcontainers-bom:1.19.3")
        }

        dependencies {
            dependency("org.learning.microservices:spring-microservices-starter:1.1.0")
            dependency("software.amazon.awssdk:s3:2.22.13")
        }
    }

    tasks.getByName<Jar>("jar") {
        archiveClassifier.set("")
    }
}

dependencies {
    implementation(project(":resource-service-api"))

    implementation("org.flywaydb:flyway-core")

    implementation("org.learning.microservices:spring-microservices-starter")

    implementation("org.postgresql:postgresql")

    implementation("org.slf4j:slf4j-api")

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
    implementation("org.springframework.cloud:spring-cloud-starter-stream-rabbit")

    implementation("org.springframework.retry:spring-retry")

    implementation("software.amazon.awssdk:s3")

    testImplementation("org.spockframework:spock-core")
    testImplementation("org.spockframework:spock-spring")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:rabbitmq")
}

tasks.getByName<Jar>("jar") {
    enabled = false
    archiveClassifier.set("")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
