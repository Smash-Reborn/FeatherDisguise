import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files

plugins {
    id("java")
    id("com.gradleup.shadow") version("8.3.0")
}

// --> packaging
group = "org.reborn"
version = "1.1.1"
description = "A lightweight non-bullshit ssm disguise plugin"

// --> java configuration
java.toolchain.languageVersion = JavaLanguageVersion.of(8)
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.codemc.io/repository/maven-releases/")           // PacketEvents
}

dependencies {
    annotationProcessor(libs.lombok)

    implementation(libs.bundles.log4j)
    implementation(libs.packetevents)
    implementation(libs.fastutil)

    // Run the BuildNMS gradle task to generate this file
    implementation(files("dependencies/spigot-1.8.8.jar"))

    compileOnly(libs.lombok)
}

// --> building
tasks.compileJava { options.encoding = "UTF-8" }
tasks.javadoc { options.encoding = "UTF-8" }
tasks.shadowJar { archiveFileName = "FeatherDisguise-plugin.jar" }
tasks.processResources { include("plugin.yml") }
tasks.build {
    dependsOn(tasks.named("BuildNMS"))
    dependsOn(tasks.processResources)
    dependsOn(tasks.shadowJar)
}

tasks.register<JavaExec>("RunBuildTools") {
    val buildToolsUrl = "https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar"
    val buildTools = File("${project.projectDir}/build/buildtools/BuildTools.jar")
    val workDir = File("${project.projectDir}/build/buildtools/")
    if (!workDir.exists()) workDir.mkdirs()
    if (!buildTools.exists()) {
        logger.info("Downloading BuildTools...")
        val connection: HttpURLConnection = URL(buildToolsUrl).openConnection() as HttpURLConnection
        connection.doInput = true
        Files.copy(connection.inputStream, buildTools.toPath())
        if (!buildTools.exists()) {
            throw Exception("Failed to retrieve BuildTools from $buildToolsUrl")
        }
        connection.disconnect()
    }
    description = "Run BuildTools"
    group = "Other"
    classpath = files("build/buildtools/BuildTools.jar")
    workingDir = workDir
    javaLauncher = javaToolchains.launcherFor(java.toolchain)
}
tasks.register("BuildNMS") {
    description = "Prepare, and build the specified versions of Spigot with NMS classes included"
    group = "Build"

    // Declare which versions of spigot to build
    val nmsVersions = listOf("1.8.8")

    val buildToolsTask = tasks.named<JavaExec>("RunBuildTools").get()
    val targetDirectory = File("${project.projectDir}/dependencies/")

    if (!targetDirectory.exists()) targetDirectory.mkdirs()
    for (version in nmsVersions) {
        val jarName = "spigot-$version.jar"
        val targetJar = targetDirectory.resolve(jarName)
        if (targetJar.exists()) {
            continue
        }
        buildToolsTask.setArgs(listOf("--rev", version, "--final-name", jarName))
        buildToolsTask.exec()
        Files.copy(File("${project.projectDir}/build/buildtools/$jarName").toPath(), targetJar.toPath())
        println("Built Spigot version $version successfully")
    }
}