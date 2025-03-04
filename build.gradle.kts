plugins {
    id("java")
    id("java-library")
    id("com.gradleup.shadow") version("8.3.0")
}

group = "org.reborn"
version = "1.0"
description = "A lightweight kinda-bullshit disguise plugin for general use"

java.toolchain.languageVersion = JavaLanguageVersion.of(8)
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8
tasks.compileJava { options.encoding = "UTF-8" }
tasks.javadoc { options.encoding = "UTF-8" }

repositories {
    mavenCentral()
    mavenLocal()        // run BuildTools and get the decompiled 1.8.8 NMS, that way you can locally fetch your dependencies for spigot shit

    //maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")         // Spigot
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.codemc.io/repository/maven-releases/")           // PacketEvents
}

dependencies {
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    api(libs.lombok)

    implementation(libs.bundles.log4j)
    implementation(libs.packetevents)
    api(libs.fastutil)

    compileOnly(files("dependencies/spigot-1.8.8.jar"))
}