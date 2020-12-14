rootProject.name = "thread"

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
    }
}

// Subprojects
include("NetLib")
include("thread-api")
include("thread-rpi-ws281x")
include("thread-examples")
include("NetLib-examples")
include("thread-network-api-common")
include("thread-network-api-server-impl")
include("thread-network-strip-api")
