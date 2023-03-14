import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly(fileTree("libs/compile"))
    implementation(fileTree("libs/implement"))
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}
