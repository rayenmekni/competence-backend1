# Design Document: Complete CI/CD Pipeline

## Overview

This design document specifies the technical architecture for implementing a complete CI/CD pipeline for the competence-backend1 Spring Boot microservice. The solution enhances the existing CI pipeline with code quality analysis (SonarCloud) and coverage reporting (JaCoCo), implements a new CD pipeline for automated Docker image deployment to Docker Hub, and orchestrates automatic triggering of CD upon successful CI completion.

### System Context

The competence-backend1 application is a Spring Boot 3.2.0 microservice built with Java 21, using Maven as the build tool and MySQL 8.0 for data persistence. The current CI pipeline successfully executes unit tests with MySQL integration but lacks code quality analysis, coverage reporting, and automated deployment capabilities.

### Design Goals

1. **Code Quality Assurance**: Integrate SonarCloud static analysis to detect code smells, bugs, vulnerabilities, and technical debt
2. **Coverage Visibility**: Implement JaCoCo code coverage measurement and reporting integrated with SonarCloud
3. **Automated Deployment**: Create CD pipeline that builds and publishes Docker images to Docker Hub
4. **Pipeline Orchestration**: Automate CD pipeline triggering upon successful CI completion using GitHub Actions workflow_run events
5. **Configuration Correctness**: Fix existing pom.xml XML structure errors and add required plugin configurations
6. **Security**: Manage sensitive credentials (SonarCloud token, Docker Hub credentials) using GitHub Secrets

### Key Technical Decisions

**Maven POM Structure**: The existing pom.xml contains duplicate `<build>` sections which is invalid XML. The design merges these sections and consolidates all plugin configurations under a single `<build>` element.

**Lombok Coverage Exclusion**: Lombok-generated code (getters, setters, constructors) inflates coverage metrics without adding value. The design uses a `lombok.config` file to instruct Lombok to add `@lombok.Generated` annotations, which JaCoCo automatically excludes from coverage analysis.

**Workflow Orchestration**: The CD pipeline uses the `workflow_run` trigger with `types: [completed]` and conditional execution based on `github.event.workflow_run.conclusion == 'success'` to ensure deployment only occurs after successful CI execution.

**Artifact Sharing**: The CI pipeline uploads the built JAR as a GitHub Actions artifact with 90-day retention. The CD pipeline downloads this artifact using the `actions/download-artifact@v4` action, ensuring version consistency between CI and CD.

**Docker Image Tagging Strategy**: Images are tagged with both `latest` (for convenience) and the commit SHA (for traceability and rollback capability).

## Architecture

### High-Level Architecture

