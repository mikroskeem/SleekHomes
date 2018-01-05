import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.TRUE
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.OP

plugins {
    kotlin("jvm") version "1.2.0"
    id("net.minecrell.licenser") version "0.3"
    id("net.minecrell.plugin-yml.bukkit") version "0.2.1"
    id("com.github.johnrengelman.shadow") version "2.0.2"
}

val gradleWrapperVersion: String by extra
val kotlinVersion: String by extra
val configurateVersion: String by extra
val h2Version: String by extra
val hikaricpVersion: String by extra
val acfPaperVersion: String by extra
val paperApiVersion: String by extra
val bstatsVersion: String by extra

repositories {
    mavenLocal()
    mavenCentral()

    maven {
        name = "destroystokyo-repo"
        setUrl("https://repo.destroystokyo.com/repository/maven-public/")
    }

    maven {
        name = "aikar-repo"
        setUrl("http://ci.emc.gs/nexus/content/groups/aikar/")
    }

    maven {
        name = "bstats-repo"
        setUrl("http://repo.bstats.org/content/repositories/releases/")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8", kotlinVersion))
    implementation("ninja.leaping.configurate:configurate-hocon:$configurateVersion") {
        exclude(module = "guava")
    }
    implementation("com.h2database:h2:$h2Version")
    implementation("com.zaxxer:HikariCP:$hikaricpVersion") {
        exclude(module = "slf4j-api")
    }
    implementation("co.aikar:acf-paper:$acfPaperVersion")
    implementation("org.bstats:bstats-bukkit:$bstatsVersion")
    compileOnly("com.destroystokyo.paper:paper-api:$paperApiVersion")

}

license {
    header = rootProject.file("etc/HEADER")
    filter.include("**/*.kt")
}

bukkit {
    name = "SleekHomes"
    main = "eu.mikroskeem.sleekhomes.Main"
    authors = listOf("mikroskeem")
    description = "A sleek homes plugin"

    permissions {
        "sleekhomes.home" {
            default = TRUE
        }

        "sleekhomes.sethome" {
            default = TRUE
        }

        "sleekhomes.delhome" {
            default = TRUE
        }

        "sleekhomes.listhomes" {
            default = TRUE
        }

        "sleekhomes.unlimited" {
            default = OP
        }
    }
}

val shadowJar by tasks.getting(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
    val relocations = listOf(
            "kotlin",
            "com.typesafe.config",
            "ninja.leaping.configurate",
            "org.bstats",
            "co.aikar",
            "net.jodah.expiringmap",
            "com.zaxxer.hikari",
            "org.h2"
    )
    val targetPackage = "eu.mikroskeem.sleekhomes.lib"

    relocations.forEach {
        relocate(it, "$targetPackage.$it")
    }

    dependencies {
        exclude("org/jetbrains/annotations/**")
        exclude("org/intellij/lang/annotations/**")
        exclude("META-INF/maven/**")
    }
}

val wrapper by tasks.creating(Wrapper::class) {
    gradleVersion = gradleWrapperVersion
    distributionUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-all.zip"
}

tasks.getByName("build").dependsOn(tasks.getByName("shadowJar"))
defaultTasks("licenseFormat", "build")