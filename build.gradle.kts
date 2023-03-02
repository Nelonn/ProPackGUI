plugins {
    id("java")
}

group = "me.nelonn.propack"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    "compileOnly"("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    "compileOnly"(files("libs/propack-bukkit-0.0.3-SNAPSHOT.jar"))
    "implementation"(files("libs/MareLib-0.0.1.jar"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
