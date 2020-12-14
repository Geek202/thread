plugins {
    java
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":NetLib"))
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.13.1")
}
