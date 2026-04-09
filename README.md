# mingsha Jenkins Plugin Build Metrics

> Jenkins Build Metrics Collection and Export Plugin

[![Jenkins](https://img.shields.io/badge/Jenkins-2.558+-blue.svg)](https://jenkins.io/)
[![Java](https://img.shields.io/badge/Java-21+-orange.svg)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](./LICENSE)

---

## Overview

This plugin collects detailed metrics from Jenkins Jobs and their build history, supporting both scheduled and manual collection. Metrics are exposed in text format via REST API for easy integration with monitoring systems like Prometheus.

---

## Features

- Collect build history, status, parameters, results, and trigger information for all Jobs
- Export metrics via REST API for Prometheus and other monitoring systems
- Support async scheduled collection and manual trigger
- Configurable collection window, namespace, and other global parameters
- Proper permission validation and security controls
- Modern code structure with comprehensive Javadoc comments
- Unit test coverage with JUnit 5 + Mockito
- SonarQube static code analysis support

---

## Requirements

- Jenkins 2.558+
- **JDK 21+** (required by hpi-plugin 3.61+)
- Maven 3.6+

---

## Quick Start

### Build

> **Note:** JDK 21+ is required to build this plugin.

```bash
# Using Maven wrapper (recommended)
./mvnw clean package

# Or using system Maven
mvn clean package

# Ensure Java 21 is available
java -version  # Should show 21.x
```

The plugin package will be generated at `target/*.hpi`.

### Run Locally (Development)

```bash
make run
```

### Install to Jenkins

1. Go to Jenkins Dashboard → Manage Jenkins → Plugin Manager → Advanced
2. Upload the `target/*.hpi` file
3. Restart Jenkins

---

## Configuration

The plugin supports global configuration (path, namespace, collection period, collection window, etc.).

Navigate to: **Manage Jenkins → System → Build Metrics**

| Parameter | Description | Default |
|-----------|-------------|---------|
| Path | API endpoint path | `build/metrics` |
| Namespace | Metrics namespace for organization | `default` |
| Collection Period | How often to collect (seconds) | `120` (2 minutes) |
| Collection Window | Time window for builds to collect (minutes) | `60` (1 hour) |

---

## API Reference

### Get Metrics

```
GET /{plugin-url}/metrics
```

**Permissions:** Requires `ADMINISTER` or `READ` permission.

**Response:**
```
Content-Type: text/plain; charset=utf-8
Cache-Control: must-revalidate,no-cache,no-store
```

**Example Response:**
```json
{
  "namespace": "default",
  "jobs": [
    {
      "jobName": "my-pipeline",
      "builds": [
        {
          "queueId": "123",
          "jobId": "45",
          "startMillis": "1712500000000",
          "duration": "60000",
          "result": "SUCCESS",
          "runner": "john.doe"
        }
      ]
    }
  ]
}
```

### Manual Collection Trigger

```
POST /{plugin-url}/collect
```

**Permissions:** Requires `ADMINISTER` permission or valid API Token.

**Response:** HTTP 200 on success, 500 on failure.

---

## Project Structure

```
src/main/java/org/jenkinsci/plugins/build/
├── api/                    # REST API endpoints
│   └── BuildMetricsAction.java
├── collector/              # Metrics collection logic
│   └── JobCollector.java
├── config/                 # Global configuration
│   └── BuildMetricsConfiguration.java
├── context/                # Dependency injection
│   └── Context.java
├── model/                  # Data models
│   └── BuildMetricsVO.java
├── service/                # Service interfaces and implementations
│   ├── BuildMetrics.java
│   ├── BuildMetricsAsyncWorker.java
│   ├── Jobs.java
│   ├── Runs.java
│   └── impl/
│       └── DefaultBuildMetrics.java
└── util/                   # Utilities
    └── ConfigurationUtils.java
```

---

## Testing

```bash
mvn test
```

Uses JUnit 5 + Mockito for comprehensive unit testing.

---

## Troubleshooting

### Java Version Error

If you encounter errors like `Unknown Java specification version for class version: 65`, ensure you have **JDK 21+** installed:

```bash
# Check current Java version
java -version

# If using SDKMAN
sdk list java | grep installed
sdk install java 21.0.2-open
sdk default java 21.0.2-open
```

---

## CI/CD

This project includes Jenkinsfiles for automated building:

- `Jenkinsfile` - Standard pipeline with build, archive, and email notification
- `Jenkinsfile.sonar` - SonarQube static code analysis

---

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Submit a Pull Request

---

## License

This project is licensed under the [MIT License](./LICENSE).

---

## Contact

- **Author:** mingsha
- **Email:** chenlong220192@gmail.com
- **GitHub:** [Project Repository](https://github.com/chenlong220192/mingsha-jenkins-plugin-build-metrics)

---

> Feel free to use and develop this plugin!
