# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Jenkins plugin that collects build metrics from Jenkins jobs and exposes them via REST API for monitoring systems like Prometheus. The plugin runs as an `@Extension` in Jenkins and supports both scheduled async collection and manual trigger.

## Build Commands

```bash
# Build (requires JDK 21+)
./mvnw clean package

# Run tests
mvn test

# Run a single test class
mvn test -D test=RunsTest

# Skip tests during build
make package SKIP_TEST=true
```

## Architecture

### Request Flow
1. `BuildMetricsAction` (REST endpoint, `UnprotectedRootAction`) → `BuildMetrics` service
2. `BuildMetrics.collectMetrics()` → `JobCollector` → iterates all Jenkins jobs → `BuildMetricsVO`
3. Result cached in `AtomicReference<String>` (JSON string) via `DefaultBuildMetrics`
4. `BuildMetricsAsyncWorker` (`AsyncPeriodicWork`) triggers periodic collection at configurable interval

### Key Components
- **BuildMetricsAction**: HTTP endpoints at `/{urlName}/{additionalPath}` (GET metrics, POST /collect)
- **BuildMetricsAsyncWorker**: Scheduled background collection using Jenkins' `AsyncPeriodicWork`
- **DefaultBuildMetrics**: Singleton service, holds cached JSON metrics via `AtomicReference`
- **JobCollector**: Iterates all Jenkins jobs via `Jobs.getAllJobs()`, filters by time window, collects build history
- **BuildMetricsConfiguration**: Global config (`GlobalConfiguration`) persisted to Jenkins config.xml

### Dependency Injection
Uses **Guice** (via `Context extends AbstractModule`). `BuildMetrics` is injected as singleton into both `BuildMetricsAction` and `BuildMetricsAsyncWorker`. Note: Jenkins extensions use field injection (`@Inject` on setters), not constructor injection.

### Data Model
`BuildMetricsVO` (namespace + jobs) → `JobMetricsVO` (jobName + builds) → `BuildVO` (queueId, jobId, startMillis, duration, result, runner). All fields are strings. Uses fluent builder pattern (`newInstance()` + setters).

### Configuration
Accessible via **Manage Jenkins → System → Build Metrics**:
- `path`: API endpoint path (default: `build/metrics`)
- `namespace`: metrics namespace (default: `default`)
- `collectingBuildMetricsPeriodInSeconds`: collection interval (default: 120s)
- `collectingBuildMetricsMinutes`: time window for builds to collect (default: 60min)

## Important Constraints

- **JDK 21+ required** to build (hpi-plugin 3.61+ requirement)
- Jenkins minimum version: **2.558**
- The plugin uses `@Extension` annotations extensively — all Jenkins components (actions, configurations, workers) must be annotated
- `BuildMetricsAction` implements `UnprotectedRootAction` (no Jenkins login required for GET, POST requires ADMINISTER)
