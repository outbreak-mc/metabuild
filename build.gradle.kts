plugins {
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

apply(plugin = "kotlin")

group = "space.outbreak.${rootProject.name}"
version = "1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

val jacksonVersion = "2.14.3"
val adventureVersion = "4.14.0"

dependencies {
    api("net.kyori:adventure-text-serializer-gson:${adventureVersion}")
    api("net.kyori:adventure-api:${adventureVersion}")
    api("net.kyori:adventure-text-minimessage:${adventureVersion}")
    api("net.kyori:adventure-text-serializer-plain:${adventureVersion}")
    api("org.apache.commons:commons-text:1.10.0")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:${jacksonVersion}")
    api("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:${jacksonVersion}")
    api("com.github.ajalt.clikt:clikt:4.2.1")
}

application {
    mainClass.set("${rootProject.group}.MainKt")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "${rootProject.group}.MainKt"
    }
}
tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.shadowJar {
    archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")
}

tasks.assemble {
    dependsOn(tasks.shadowJar)
}