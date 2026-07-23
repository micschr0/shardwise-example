# CLAUDE.md — shardwise-example Conventions

A public example build whose only job is to make
[Shardwise](https://github.com/micschr0/gradle-test-shard-plugin) legible: twelve modules,
deliberately unbalanced test durations, and a CI run where a single-runner job sits next
to a four-runner matrix so the two durations can be compared directly.

**This repository is a demonstration, not an application.** Every file must earn its place
by making the sharding behaviour clearer. Code that would be good practice in a real
product but adds nothing here is noise.

## NEVER

- NEVER use `Thread.sleep` to create test duration — sleeping costs no CPU, so any
  scheduler looks good against it and the measurement becomes a lie. Use `Workload`.
- NEVER hand-edit the numbers in `test-weights.properties` — they are measured output
- NEVER add a `newcomer` entry to `test-weights.properties` — its absence IS the
  coverage-beats-balance demonstration
- NEVER give `bench-support` a test — its emptiness IS the no-test-task demonstration
- NEVER run the measurement pass with the build cache on — a cached `test` task reports
  no duration and would produce a weights file full of zeroes
- NEVER add a module without deciding which demonstration it serves
- NEVER modify `.github/workflows/*.yml` without running `actionlint`
- NEVER commit `mavenLocal()` or an included build into `settings.gradle.kts` — the
  example MUST resolve the plugin the way a real user does
- NEVER quote CI durations in `README.md` that were not actually measured

## ALWAYS

- ALWAYS use conventional commit messages (`type: subject`)
- ALWAYS pin GitHub Actions to a full commit SHA with the version in a trailing comment
- ALWAYS set `persist-credentials: false` on `actions/checkout`
- ALWAYS declare `permissions: {}` at workflow top level and re-grant per job
- ALWAYS make `step-security/harden-runner` (egress-policy: audit) the first step of every job
- ALWAYS re-measure and update `test-weights.properties` after changing any test's workload
- ALWAYS keep every module listed in `settings.gradle.kts` reachable by exactly one node
  in a four-node plan — verify with the plan dump below
- ALWAYS explain a demonstration in `README.md` when adding it to a module

## Build Commands

The plugin is not yet resolvable from the Gradle Plugin Portal, so local verification
needs it in `mavenLocal` plus an init script that is deliberately **not** committed:

```bash
# once, from the plugin repository:
./gradlew publishToMavenLocal

# init script, kept outside this repository:
cat > /tmp/local-portal.init.gradle.kts <<'EOF'
settingsEvaluated {
    pluginManagement {
        repositories { mavenLocal(); gradlePluginPortal() }
    }
}
EOF
```

```bash
INIT=-I/tmp/local-portal.init.gradle.kts

# Baseline: the whole suite on one runner. CI_NODE_* must be UNSET, not empty.
./gradlew test --no-build-cache --no-daemon $INIT

# One shard.
CI_NODE_INDEX=2 CI_NODE_TOTAL=4 ./gradlew test --no-daemon $INIT

# What each node decided, without running anything (fast).
for n in 1 2 3 4; do
  CI_NODE_INDEX=$n CI_NODE_TOTAL=4 ./gradlew test --dry-run --no-daemon $INIT
done

# Re-measure and rewrite test-weights.properties.
./gradlew test --no-build-cache --no-daemon $INIT
./gradlew generateTestWeights --no-daemon $INIT

# Imbalance statistics and the heaviest modules.
./gradlew shardwiseAnalyze --no-daemon $INIT

actionlint .github/workflows/*.yml
```

## Layout

```text
build.gradle.kts                   applies the plugin, spells out its defaults
settings.gradle.kts                the twelve modules
test-weights.properties            measured milliseconds, committed
buildSrc/…/bench-module.gradle.kts one convention plugin, applied by every test module
bench-support/                     Workload — the CPU burner, no tests of its own
<domain modules>/                  one test class each, calling Workload
```

Module build files stay at one line (`plugins { id("bench-module") }`). A module that
needs more than that is either a new demonstration — document it — or scope creep.

## Invariants

- **Honest workload.** Durations come from chained SHA-256 rounds in `Workload`.
  `Workload.UNIT` is the single calibration knob, sized so one unit costs roughly one
  second on a GitHub-hosted runner.
- **The win must be visible.** Every node compiles every module; Shardwise skips the
  `test` task, not its dependencies. That fixed cost is paid four times over and can
  swallow the balancing gain entirely. If baseline and slowest shard land within noise of
  each other, the example is arguing against its own plugin — grow the test workload
  until the difference is unmistakable, and say so in `README.md` rather than hiding it.
- **Four demonstrations, four modules.** `catalog` dominates (the wall-time floor),
  `newcomer` is missing from the weights, `bench-support` has no tests, `common-kotlin`
  is Kotlin. Changing one of these breaks a section of `README.md`.
- **Weights churn.** The generated file carries a timestamp comment, so `git diff
  --quiet` is never empty. `weights-refresh.yml` compares the measurement lines only.
  Any tooling that diffs this file must do the same.
- **Failing loudly is correct.** An invalid `CI_NODE_INDEX` / `CI_NODE_TOTAL` must fail
  the build immediately. A sharding tool that silently runs nothing is the actual
  disaster; a red build is not.

## Known Gaps

- `README.md` and `build.gradle.kts` pin `0.4.1` — the latest version actually resolvable
  from the Gradle Plugin Portal. Later tags may exist on GitHub before they reach the
  Portal; confirm the pin resolves (`maven-metadata.xml` is public) before bumping.
- `renovate.yml` needs a GitHub App installed on this repo with the
  `RENOVATE_APP_ID` / `RENOVATE_PRIVATE_KEY` secrets set. Without them that workflow
  fails at the token step; the rest of CI is unaffected.
- No aggregation of the four nodes' test reports into one view. Each node uploads its own
  artifact. Worth adding only if someone asks for it.
