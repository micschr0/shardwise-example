// Deliberately test-free. A module with no `test` task must not confuse the
// shard planner — this module is the standing proof that it doesn't.

plugins {
    `java-library`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
