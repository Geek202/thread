plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    api("io.netty:netty-all:4.1.55.Final")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    api("org.slf4j:slf4j-api:1.7.30")
    api("it.unimi.dsi:fastutil:8.2.1")
}
