plugins {
    java
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow") version("6.1.0")
}

repositories {
    mavenCentral()
}

application {
    mainClassName = "me.geek.tom.thread.api.example.NetworkedHueFadeKt"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":thread-api"))
    implementation(project(":thread-rpi-ws281x"))
    implementation(project(":thread-network-strip-api"))
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.13.1")
}

// shadowJar breaks without this
project.setProperty("mainClassName", "me.geek.tom.thread.api.example.NetworkedHueFadeKt")

tasks.build {
    dependsOn(tasks.shadowJar)
}
