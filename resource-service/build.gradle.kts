plugins {
    id("java")
    id("org.springframework.boot") version "3.1.4"
}

apply(plugin = "io.spring.dependency-management")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.flywaydb:flyway-core")

    implementation("org.postgresql:postgresql")

    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    implementation("org.slf4j:slf4j-api")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
}

tasks.getByName<Jar>("jar") {
    enabled = false
}

tasks.test {
    useJUnitPlatform()
}
