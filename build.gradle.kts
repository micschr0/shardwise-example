plugins {
    id("de.micschro.shardwise") version "0.4.1"
    kotlin("jvm") version "2.4.0" apply false
}

// Everything below is default behaviour, spelled out so this file doubles as
// documentation. Deleting the whole `shardwise { }` block changes nothing.
shardwise {
    // Which task names get their own independent shard plan.
    taskNames.set(setOf("test"))

    // Modules absent from the weights file get this many milliseconds.
    // Deliberately generous: an unknown module must never look cheap enough
    // to be starved onto an already-full node.
    defaultWeight.set(10_000)

    // Committed alongside the code. Refreshed nightly by .github/workflows/weights-refresh.yml.
    weightsFile.set(layout.projectDirectory.file("test-weights.properties"))
}
