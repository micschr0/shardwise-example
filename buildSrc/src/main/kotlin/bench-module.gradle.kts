// Convention applied by every module that has tests. A convention plugin rather
// than `subprojects { }` in the root build, because that is how a real build of
// this size stays readable: each module says what it is, in one line.
//
// Note this does not buy Isolated Projects — Shardwise itself reads every
// project to build its plan and is deliberately incompatible with that feature.
// Configuration caching works fine.

plugins {
    `java-library`
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    "testImplementation"(project(":bench-support"))
    "testImplementation"("org.junit.jupiter:junit-jupiter:5.13.4")
    "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
