plugins {
    java
    kotlin("jvm") version "1.4.10" apply false
}

val ENV: Map<String, String> = System.getenv()
var build = "local"
if (ENV["BUILD_NUMBER"] != null) {
    build = ENV["BUILD_NUMBER"]?: error("")
}

val baseVersion = "1.0"

allprojects {
    apply(plugin = "maven-publish")

    group = "me.geek.tom"
    version = "$baseVersion+build.$build"

    repositories {
        maven {
            name = "TomTheGeek"
            url = uri("https://maven.tomthegeek.ml/")
        }
        mavenCentral()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
        kotlinOptions.jvmTarget = "1.8"
    }
}

subprojects {
    if (!name.endsWith("examples")) {
        tasks.create<Jar>("sourcesJar") {
            archiveClassifier.set("sources")
        }

        apply(plugin = "maven-publish")

        val ENV = System.getenv()

        afterEvaluate {
            tasks["sourcesJar"].dependsOn(tasks["compileKotlin"])
            (tasks["sourcesJar"] as Jar).from(sourceSets.main.get().allSource)

            extensions.getByType<PublishingExtension>().publications {
                create<MavenPublication>("mavenJava") {
                    from(components["java"])

                    artifact(tasks["sourcesJar"]) {
                        builtBy(tasks["sourcesJar"])
                    }
                }
            }

            extensions.getByType<PublishingExtension>().repositories {
                if (ENV["MAVEN_USER"] != null) {
                    maven {
                        url = uri("https://maven-upload.tomthegeek.ml/")
                        name = "TomTheGeek-repo"
                        credentials {
                            username = ENV["MAVEN_USER"]
                            password = ENV["MAVEN_PASS"]
                        }
                    }
                }
            }
        }
    }
}
