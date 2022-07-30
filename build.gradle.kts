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
        url = uri("https://m2.dv8tion.net/releases")
    }
}

dependencies {
    implementation("com.discord4j:discord4j-core:3.2.2")
    implementation("com.sedmelluq:lavaplayer:1.3.77")
    implementation("org.projectlombok:lombok:1.18.24")
    implementation("org.json:json:20220320")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")

    runtimeOnly("org.slf4j:slf4j-simple:1.7.36")
    runtimeOnly("org.slf4j:slf4j-api:1.7.36")

    annotationProcessor("org.projectlombok:lombok:1.18.24")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.24")
}

tasks.withType<Jar>() {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = "de.pbz.rundfunk.Rundfunk"
    }
    configurations["compileClasspath"]
        .forEach { file: File ->
            from(zipTree(file.absoluteFile))
        }
}
