plugins {
    idea
    java
    application
    id("com.github.johnrengelman.shadow").version("7.0.0")
    id("com.github.ben-manes.versions").version("0.39.0")
    id("io.freefair.lombok").version("6.1.0-m3")
    id("com.google.cloud.tools.jib").version("3.1.2")
    id("com.gorylenko.gradle-git-properties").version("2.3.1")
    id("de.jjohannes.extra-java-module-info").version("0.9")
}

group = "fr.raksrinana"
description = "RSNDiscord"

dependencies {
    implementation(libs.jda) {
        exclude(module = "opus-java")
    }
    implementation("club.minnced:opus-java:1.1.0@pom") {
        isTransitive = true
    }
    implementation(libs.lavaplayer)
    implementation(libs.lpCross)
    implementation(libs.jump3r)

    implementation(libs.slf4j)
    implementation(libs.bundles.log4j2)

    implementation(libs.unirest)
    implementation(libs.picocli)
    implementation(libs.bundles.jackson)
    implementation(libs.httpclient)
    implementation(libs.lang3)
    implementation(libs.reflections)
    implementation(libs.twittered)
    implementation(libs.kittehIrc)

    compileOnly(libs.jetbrainsAnnotations)
}

repositories {
    maven {
        name = "m2-dv8tion"
        url = uri("https://m2.dv8tion.net/releases")
    }
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
    mavenCentral()
}

tasks {
    processResources {
        expand(project.properties)
    }

    compileJava {
        val moduleName: String by project
        inputs.property("moduleName", moduleName)

        options.encoding = "UTF-8"
        options.isDeprecation = true

        doFirst {
            val compilerArgs = options.compilerArgs

            val path = classpath.asPath.split(";")
                .filter { it.endsWith(".jar") }
                .joinToString(";")
            compilerArgs.add("--module-path")
            compilerArgs.add(path)
            classpath = files()
        }
    }

    jar {
        manifest {
            attributes["Multi-Release"] = "true"
        }
    }

    test {
        useJUnitPlatform()
    }

    shadowJar {
        archiveBaseName.set(project.name)
        archiveClassifier.set("shaded")
        archiveVersion.set("")
    }

    wrapper {
        val wrapperVersion: String by project
        gradleVersion = wrapperVersion
    }
}

application {
    val moduleName: String by project
    val className: String by project

    mainModule.set(moduleName)
    mainClass.set(className)
}

extraJavaModuleInfo {
    failOnMissingModuleInfo.set(false)
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16

    modularity.inferModulePath.set(false)
}

jib {
    from {
        image = "adoptopenjdk:16-jre"
        platforms {
            platform {
                os = "linux"
                architecture = "arm64"
            }
        }
    }
    to {
        image = "mrcraftcod/rsn-discord"
        auth {
            username = project.findProperty("dockerUsername").toString()
            password = project.findProperty("dockerPassword").toString()
        }
    }
}
