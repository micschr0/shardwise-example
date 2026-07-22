# Shardwise, in a build you can actually run

A twelve-module Gradle build wired up with
[Shardwise](https://github.com/micschr0/gradle-test-shard-plugin), so you can see
runtime-balanced test sharding working before deciding whether you want it.

Every CI run here does the same suite twice: once on a single runner, once split
across four. **Open the latest run in the Actions tab and compare the two job
durations.** That comparison is the whole point of this repository.

---

## What the plan looks like

Same command on every node — `./gradlew test` — with two environment variables
set differently. Each node prints what it decided to run:

```text
Node 1 of 4    catalog
Node 2 of 4    orders, shipping, search, reporting
Node 3 of 4    payments, inventory, pricing
Node 4 of 4    bench-support, newcomer, notifications, common-kotlin
```

Measured locally — cold `build/`, warm Gradle home, `--no-build-cache`, no daemon:

| | wall clock | of which test tasks |
|---|---|---|
| One runner, all twelve modules | **179 s** | 91.5 s |
| Four runners, slowest node | **99 s** | 25–27 s |

**1.8x on four runners.** Not 4x, and the gap between those two columns is why.

Roughly 75 seconds of every run is fixed cost: starting Gradle, building `buildSrc`,
compiling all twelve modules. Shardwise skips the `test` task, not the tasks it
depends on, so each node still compiles the whole build. That overhead is paid
four times over and no scheduler can divide it.

The test work itself divides nearly perfectly — 91.5 s across four nodes lands at
25–27 s each. What it cannot go below is `catalog`, which takes 25.8 s on its own
and cannot be split. That is the floor, and it is worth knowing before paying for
a fifth runner. `./gradlew shardwiseAnalyze` reports the imbalance behind it:
3.05x, with `catalog` holding 30.4% of the total.

So the honest rule: sharding pays when your test time is large next to your
compile time. Measure both before buying runners.

---

## The four situations this repository exists to show

**One module dominates.** `catalog` is 30% of the suite on its own. Splitting by
module count would put three cheap modules next to it and leave a runner idle for
half the build. Splitting by measured weight puts `catalog` alone on node 1 and
packs the rest to match.

**A module the weights file has never heard of.** `newcomer` is deliberately
absent from `test-weights.properties` — it stands for the module a colleague
added after the last refresh. It still gets planned, onto node 4, using
`defaultWeight`. This is the guarantee that matters: the failure mode of a
sharding tool is not a slow build, it is a module that runs on zero nodes while
CI reports green.

**A module with no tests at all.** `bench-support` has no `test` task. It appears
in the plan and costs nothing. Nothing to configure.

**Mixed languages.** `common-kotlin` is Kotlin, the rest is Java. The planner
works on module paths and does not care.

---

## The weights file goes stale, so refresh it on a schedule

`test-weights.properties` holds measured milliseconds per module. It is only as
good as its last measurement — nothing breaks when it rots, the nodes just drift
apart again until you are back where you started.

[`weights-refresh.yml`](.github/workflows/weights-refresh.yml) runs the suite
unsharded once a week and opens a pull request when the numbers have moved. This
is the part most integrations leave out.

Two details in that workflow are worth stealing:

- The measurement run needs `--no-build-cache`. A cached test task reports no
  duration, and you would commit a weights file full of zeroes.
- The generated file carries a timestamp comment, so `git diff --quiet` is never
  empty. Compare the measurement lines, or you will open a pull request every
  week that changes nothing.

---

## Copying this into your own build

Three things, and none of them touch your modules.

**1. Apply the plugin to the root project:**

```kotlin
// build.gradle.kts
plugins {
    id("de.micschro.shardwise") version "0.4.1"
}
```

**2. Measure once and commit the result:**

```bash
./gradlew test --no-build-cache
./gradlew generateTestWeights      # writes test-weights.properties
```

**3. Set two environment variables per CI job** — see
[`ci.yml`](.github/workflows/ci.yml) for the matrix:

```yaml
env:
  CI_NODE_INDEX: ${{ matrix.node }}   # 1-based
  CI_NODE_TOTAL: '4'
```

That is the entire integration. Removing it is deleting the plugins line.

---

## About the workload

The tests here do real, deterministic CPU work — chained SHA-256 rounds in
[`bench-support`](bench-support/src/main/java/bench/Workload.java) — rather than
`Thread.sleep`. Sleeping would make the numbers meaningless: sleeping tasks cost
no CPU and any scheduler looks good against them.

The durations are chosen to be visibly unbalanced, because that is the situation
Shardwise addresses. A suite where every module costs the same has nothing to
gain here. The domain names are decoration; there is no application under them.

`Workload.UNIT` is the calibration knob. It is set so one unit costs roughly one
second on a GitHub-hosted runner. On very different hardware the whole suite
scales with it.

---

## License

Apache-2.0, same as the plugin.
