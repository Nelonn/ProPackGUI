import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id("io.github.goooler.shadow") version "8.1.8"
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") // Paper API
    maven("https://repo.codemc.io/repository/maven-public/") // NBT API
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.4-R0.1-SNAPSHOT")
    compileOnly(fileTree("libs/compile"))
    implementation(fileTree("libs/implement"))
    implementation("de.tr7zw:item-nbt-api:2.14.1")
}

tasks.named<JavaCompile>("compileJava") {
    options.encoding = "UTF-8"
}

tasks.named<Copy>("processResources") {
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand("version" to version)
    }
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    relocate("me.nelonn.commandlib", "me.nelonn.propack.bukkitgui.commandlib")
    relocate("de.tr7zw.changeme.nbtapi", "me.nelonn.propack.bukkitgui.nbtapi")
}

tasks.named("assemble").configure {
    dependsOn("shadowJar")
}
