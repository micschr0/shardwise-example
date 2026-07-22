# Continuous Integration

The CI configuration is part of the product here. Visitors copy these workflows, so they
have to be both correct and worth imitating.

## Requirement: Every run MUST show the comparison

The value of runtime-balanced sharding is only legible side by side. Every CI run MUST
execute the suite unsharded on one runner and sharded across four, so the two durations
appear next to each other in the Actions UI.

The baseline job MUST run with `CI_NODE_INDEX` and `CI_NODE_TOTAL` unset, so the plugin
stays out of the way entirely.

### Scenario: A visitor opens the latest run

- **Given** a push or pull request has triggered CI
- **When** a visitor opens the run in the Actions tab
- **Then** they see one `baseline` job and four `sharded` jobs
- **And** each job's duration is visible without opening its logs

### Scenario: The shards run an identical command

- **Given** the four matrix jobs
- **When** their step definitions are compared
- **Then** they differ only in `CI_NODE_INDEX`
- **And** each runs plain `./gradlew test`

## Requirement: A failing shard MUST NOT hide the others

If one node fails, the remaining nodes' durations are exactly the evidence needed to
diagnose it.

The matrix MUST set `fail-fast: false`.

### Scenario: One node fails

- **Given** the tests on node 3 fail
- **When** the run completes
- **Then** nodes 1, 2, and 4 have still run to completion and reported their durations

## Requirement: Each node MUST publish the reports it produced

A sharded node only produces reports for the modules it ran. Reconstructing a full suite
report MUST be possible from the run's artifacts.

### Scenario: Reports survive a red build

- **Given** a node's tests fail
- **When** the job finishes
- **Then** that node's test reports are uploaded anyway
- **And** the upload fails the step if it finds no files, rather than passing silently

## Requirement: Weights MUST be refreshed on a schedule

A stale weights file breaks nothing loudly. The nodes simply drift apart again until the
build is back where it started, which is the failure mode nobody notices.

A scheduled workflow MUST measure the suite unsharded and open a pull request when the
measurements have moved.

### Scenario: The measurement pass is uncached

- **Given** the scheduled refresh runs
- **When** it executes the suite
- **Then** it passes `--no-build-cache`, because a cached test task reports no duration

### Scenario: A timestamp alone does not open a pull request

- **Given** the generated file carries a timestamp comment that changes on every run
- **When** the measured milliseconds are unchanged
- **Then** no pull request is opened

### Scenario: Moved measurements reach a human

- **Given** at least one module's measured duration has changed
- **When** the refresh completes
- **Then** a pull request is opened against the default branch
- **And** its body explains that a large move usually means a suite changed shape

## Requirement: Workflows MUST be hardened

Visitors copy this configuration into builds that matter.

Every action MUST be pinned to a full commit SHA with its version in a trailing comment.
`actions/checkout` MUST set `persist-credentials: false`. Every job MUST declare a
timeout and the narrowest `permissions` it can. Credentials MUST NOT be written into
`.git/config`; a job that pushes MUST use a one-shot authenticated URL.

### Scenario: The workflows pass their linter

- **Given** any change under `.github/workflows/`
- **When** `actionlint .github/workflows/*.yml` runs
- **Then** it reports no findings

### Scenario: No action is pinned by tag

- **Given** any `uses:` line
- **When** it is inspected
- **Then** it names a 40-character commit SHA, followed by a comment naming the version

## Requirement: The example MUST resolve the plugin as a user would

Local verification may inject `mavenLocal()` through an init script, but the committed
build MUST depend on the plugin exactly the way a stranger's build would.

### Scenario: No local escape hatch is committed

- **Given** `settings.gradle.kts` and `build.gradle.kts`
- **When** they are inspected
- **Then** neither declares `mavenLocal()`, an included build, nor a composite build
- **And** the plugin is applied by id and version alone
