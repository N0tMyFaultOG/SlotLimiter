import org.cadixdev.gradle.licenser.LicenseExtension
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    id("org.cadixdev.licenser") version "0.5.0"
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
    kotlin("jvm") version "1.4.21"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = sourceCompatibility
}

repositories {
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots") }
    maven { url = uri("https://papermc.io/repo/repository/maven-snapshots/") }
    mavenCentral()
}

dependencies {
    compileOnly("net.luckperms:api:5.2")
    compileOnly("me.lucko:helper:5.6.5")
    compileOnly("com.destroystokyo.paper:paper-api:1.16.4-R0.1-SNAPSHOT")
}

version = "1.0.0"

configure<LicenseExtension> {
    header = rootProject.file("LICENSE-HEADER")
    include("**/*.java")
    newLine = false
}

bukkit {
    name = "SlotLimiter"
    main = "de.n0tmyfaultog.slotlimiter.SlotLimiterPlugin"
    apiVersion = "1.13"
    author = "NotMyFault"
    depend = listOf("LuckPerms", "helper")

    permissions {
        register("slotlimiter.bypass") {
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "11"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "11"
}