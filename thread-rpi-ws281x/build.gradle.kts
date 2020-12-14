plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    api("com.github.mbelling:rpi-ws281x-java:2.0.0-SNAPSHOT")
    api(project(":thread-api"))
}
