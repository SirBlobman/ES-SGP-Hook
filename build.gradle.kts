val baseVersion = fetchProperty("version.base", "invalid")
val betaString = fetchProperty("version.beta", "false")
val jenkinsBuildNumber = fetchEnv("BUILD_NUMBER", null, "Unofficial")

val betaBoolean = betaString.toBoolean()
val betaVersion = if (betaBoolean) "Beta-" else ""
version = "$baseVersion.$betaVersion$jenkinsBuildNumber"

fun fetchProperty(propertyName: String, defaultValue: String): String {
    val found = findProperty(propertyName)
    if (found != null) {
        return found.toString()
    }

    return defaultValue
}

fun fetchEnv(envName: String, propertyName: String?, defaultValue: String): String {
    val found = System.getenv(envName)
    if (found != null) {
        return found
    }

    if (propertyName != null) {
        return fetchProperty(propertyName, defaultValue)
    }

    return defaultValue
}

plugins {
    id("java")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral() // Maven Central

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // SpigotMC Repository
    maven("https://oss.sonatype.org/content/repositories/snapshots/") // OSS Sonatype Snapshots
    maven("https://repo.songoda.com/repository/minecraft-plugins/") // Songoda Repository
    maven("https://nexus.sirblobman.xyz/proxy-jitpack/") // JitPack Proxy
}

dependencies {
    // Java Dependencies
    compileOnly("org.jetbrains:annotations:26.0.2") // JetBrains Annotations
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT") // Spigot API

    // Plugin Dependencies
    compileOnly("com.songoda:EpicSpawners-API:1.0.0-SNAPSHOT") // EpicSpawners API
    compileOnly("com.github.brcdev-minecraft:shopgui-api:3.0.0") // ShopGUIPlus API
}

tasks {
    named<Jar>("jar") {
        archiveBaseName.set("ES-SGP-Hook")
    }

    processResources {
        val pluginName = fetchProperty("bukkit.plugin.name", "")
        val pluginPrefix = fetchProperty("bukkit.plugin.prefix", "")
        val pluginDescription = fetchProperty("bukkit.plugin.description", "")
        val pluginWebsite = fetchProperty("bukkit.plugin.website", "")
        val pluginMainClass = fetchProperty("bukkit.plugin.main", "")

        filesMatching("plugin.yml") {
            expand(
                mapOf(
                    "pluginName" to pluginName,
                    "pluginPrefix" to pluginPrefix,
                    "pluginDescription" to pluginDescription,
                    "pluginWebsite" to pluginWebsite,
                    "pluginMainClass" to pluginMainClass,
                    "pluginVersion" to version
                )
            )
        }
    }
}
