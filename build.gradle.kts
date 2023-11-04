import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
    maven("https://repo.codemc.io/repository/maven-public/") // NBT API
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    compileOnly(fileTree("libs/compile"))
    implementation(fileTree("libs/implement"))
    implementation("de.tr7zw:item-nbt-api:2.12.0")
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    relocate("me.nelonn.commandlib", "me.nelonn.propack.bukkitgui.commandlib")
    relocate("de.tr7zw.changeme.nbtapi", "me.nelonn.propack.bukkitgui.nbtapi")
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}
