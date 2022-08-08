import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "me.soren"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    //maven {
    //    url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap")
    //}
}

val ktor_version: String by project

dependencies {
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")

    testImplementation(kotlin("test"))
    testImplementation("com.google.truth:truth:1.1.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}