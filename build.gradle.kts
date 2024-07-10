plugins {
    java
    application
}

group = "de.pbz"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        name = "arbjergDevSnapshots"
        url = uri("https://maven.lavalink.dev/snapshots")
    }
}

dependencies {
    implementation("com.discord4j:discord4j-core:3.2.6")
    implementation("dev.arbjerg:lavaplayer:2.2.1")
    implementation("org.json:json:20231013")

    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("org.slf4j:slf4j-simple:2.0.12")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
}

application {
    mainClass.set("de.pbz.rundfunk.Rundfunk")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "de.pbz.rundfunk.Rundfunk"
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from({
        configurations.runtimeClasspath.get()
                .filter { it.name.endsWith("jar") }
                .map { zipTree(it) }
    })
}
