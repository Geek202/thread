plugins {
    `java-library`
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    api(project(":thread-api"))
    api(project(":thread-network-api-common"))
}
