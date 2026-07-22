# Demonstration

What this repository has to prove to a visitor who has never used Shardwise. Each
requirement is bound to something in the build, so a reviewer can check it rather than
take the README's word for it.

## Requirement: The suite MUST be visibly unbalanced

A suite where every module costs the same has nothing to gain from runtime-balanced
sharding. This repository MUST present the situation the plugin addresses.

One module MUST hold at least 25% of the total measured weight, and the ratio between the
heaviest module and the mean MUST be at least 2.5x.

### Scenario: The imbalance is reported

- **Given** `test-weights.properties` reflects a recent measurement
- **When** a reader runs `./gradlew shardwiseAnalyze`
- **Then** the output reports an `imbalance` of at least 2.5x
- **And** the heaviest module accounts for at least 25% of the total

## Requirement: Test duration MUST come from real CPU work

Sleeping tasks consume no CPU, so they make any scheduler look good and turn every
measurement in the README into a claim the reader cannot trust.

Test duration MUST be produced by `bench-support`'s `Workload`. No test may call
`Thread.sleep`, and no build file may introduce an artificial delay.

### Scenario: No sleeping tests

- **Given** the repository at any commit
- **When** a reviewer greps the sources for `Thread.sleep`
- **Then** there are no matches outside documentation that explains why

### Scenario: The workload has one calibration knob

- **Given** hardware substantially faster or slower than a GitHub-hosted runner
- **When** a maintainer adjusts `Workload.UNIT`
- **Then** every module's duration scales with it, and no test file needs editing

## Requirement: A module absent from the weights file MUST still run

The failure that matters is not a slow build. It is a module that runs on zero nodes
while CI reports green. This repository MUST keep a standing demonstration that
Shardwise's default errs toward running.

The module `newcomer` MUST NOT appear in `test-weights.properties`, and MUST appear in
exactly one node's plan for any node count.

### Scenario: The unknown module is planned anyway

- **Given** `newcomer` has no entry in `test-weights.properties`
- **When** the plans for nodes 1 through 4 of 4 are dumped
- **Then** `newcomer` appears in exactly one of them
- **And** it is weighted with the configured `defaultWeight`

### Scenario: A scheduled refresh does not silently end the demonstration

- **Given** the weekly refresh workflow has measured `newcomer` and opened a pull request
- **When** a maintainer reviews that pull request
- **Then** the `newcomer` line is dropped before merging
- **And** `test-weights.properties` explains this in a comment at the top of the file

## Requirement: A module with no tests MUST NOT disturb the plan

Real builds contain modules that produce no test task at all. `bench-support` MUST remain
one of them.

### Scenario: The test-free module is harmless

- **Given** `bench-support` declares no tests
- **When** any node builds its plan
- **Then** the plan is produced without error
- **And** `bench-support` contributes no measurable time to the node that owns it

## Requirement: The planner MUST be shown to be language-agnostic

Shardwise plans by module path. `common-kotlin` MUST stay written in Kotlin while every
other test module stays Java, so this is visible rather than merely asserted.

### Scenario: A Kotlin module is planned like any other

- **Given** `common-kotlin` contains only Kotlin tests
- **When** the plans for four nodes are dumped
- **Then** `common-kotlin` appears in exactly one of them

## Requirement: Every module MUST land on exactly one node

Coverage beats balance. No module may be dropped, and none may run twice.

### Scenario: The plans partition the build

- **Given** the modules listed in `settings.gradle.kts`
- **When** the plans for nodes 1 through N of N are collected, for N in 1, 2, 4, and a
  number greater than the module count
- **Then** their union is every module
- **And** their pairwise intersections are empty

## Requirement: An invalid node configuration MUST fail the build

A sharding tool that quietly runs nothing is worse than a red build.

### Scenario: The build refuses a nonsensical node index

- **Given** `CI_NODE_TOTAL` is 4
- **When** `CI_NODE_INDEX` is set to `0`, to `abc`, or to `5`
- **Then** the build fails immediately with a message naming the variable

### Scenario: Empty is not the same as unset

- **Given** `CI_NODE_INDEX` and `CI_NODE_TOTAL` are set to the empty string
- **When** the build runs
- **Then** it fails loudly rather than silently falling back to the unsharded path

## Requirement: The README MUST report only measured numbers

Every duration printed in `README.md` MUST come from a run someone actually performed,
and MUST name the conditions under which it was measured.

### Scenario: A number can be traced

- **Given** any duration quoted in `README.md`
- **When** a reader asks where it came from
- **Then** the surrounding text names the command and the conditions
- **And** no number is presented as a CI result until CI has produced it

## Requirement: The sharded win MUST exceed the fixed cost

Shardwise skips the `test` task, not the tasks it depends on. Every node therefore
compiles every module and pays that cost in full. If the balancing gain disappears into
that overhead, this repository argues against the plugin it exists to explain.

The wall-clock time of the slowest shard MUST be measurably below the unsharded baseline
under identical conditions.

### Scenario: Baseline and slowest shard are compared under equal conditions

- **Given** a cold `build/` directory and a warm Gradle user home
- **When** the unsharded baseline and each of four shards are timed end to end
- **Then** the slowest shard is meaningfully faster than the baseline
- **And** `README.md` reports both numbers, together with the fixed compile cost that
  separates them from the pure test-task figures
