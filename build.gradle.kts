plugins {
    java
    kotlin("jvm") version "1.4.10" apply false
    id("org.jetbrains.dokka") version "1.4.20"
}

val ENV: Map<String, String> = System.getenv()
var build = "local"
if (ENV["BUILD_NUMBER"] != null) {
    build = ENV["BUILD_NUMBER"]?: error("")
}

val baseVersion = "1.0"

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(buildDir.resolve("docs"))
}

tasks.register<Copy>("copyDocumentationIndex") {
    dependsOn(tasks.dokkaHtmlMultiModule.get())
    from(project.file("doc_index.html"))
    rename {
        "index.html"
    }
    into(buildDir.resolve("docs"))
}

tasks.register("docs") {
    dependsOn(tasks["copyDocumentationIndex"])
}

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
        jcenter()
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
        apply(plugin = "org.jetbrains.dokka")

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

            tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
                outputDirectory.set(buildDir.resolve("dokka"))
            }
        }
    }
}
