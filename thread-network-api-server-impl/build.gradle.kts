plugins {
    java
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

application {
    mainClassName = "me.geek.tom.thread.api.network.server.ThreadLedServerKt"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(project(":thread-api"))
    implementation(project(":thread-rpi-ws281x"))
    implementation(project(":thread-network-api-common"))
    implementation("com.uchuhimo:konf-toml:0.23.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.13.1")
}
