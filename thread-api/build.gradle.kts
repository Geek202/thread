plugins {
    `java-library`
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    api("org.slf4j:slf4j-api:1.7.30")

    testImplementation(kotlin("test-junit"))
}
