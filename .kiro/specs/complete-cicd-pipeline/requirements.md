# Requirements Document: Complete CI/CD Pipeline

## Introduction

This document specifies the requirements for completing the DevOps CI/CD pipeline for the competence-backend1 Spring Boot microservice. The system will enhance the existing CI pipeline with code quality analysis and coverage reporting, implement a CD pipeline for Docker image deployment, and automate the pipeline orchestration to meet academic requirements for DevOps coursework.

The current CI pipeline successfully executes unit tests with MySQL integration. This feature will add mandatory static code analysis with SonarCloud, code coverage reporting with JaCoCo, automated Docker image building and publishing to Docker Hub, and automatic triggering of the CD pipeline upon successful CI completion.

## Glossary

- **CI_Pipeline**: The Continuous Integration workflow that builds, tests, and analyzes code quality on every push to the main branch
- **CD_Pipeline**: The Continuous Deployment workflow that builds Docker images and publishes them to Docker Hub
- **SonarCloud**: Cloud-based static code analysis platform that evaluates code quality, security vulnerabilities, and technical debt
- **JaCoCo**: Java Code Coverage library that measures and reports test coverage metrics
- **Coverage_Report**: A document showing the percentage of code executed by automated tests
- **Docker_Image**: A packaged, executable artifact containing the application and its runtime dependencies
- **Docker_Hub**: A cloud-based registry service for storing and distributing Docker images
- **GitHub_Actions**: The CI/CD automation platform integrated with GitHub repositories
- **Maven**: The build automation tool used for compiling, testing, and packaging the Spring Boot application
- **Workflow_Run_Trigger**: A GitHub Actions event that triggers one workflow when another workflow completes
- **Quality_Gate**: A set of threshold conditions in SonarCloud that must be met for code to be considered acceptable
- **JAR_Artifact**: The compiled Java application packaged as a Java Archive file
- **GitHub_Secrets**: Encrypted environment variables stored in GitHub repository settings for sensitive credentials

## Requirements

### Requirement 1: Static Code Analysis Integration

**User Story:** As a developer, I want automated static code analysis on every code push, so that code quality issues and security vulnerabilities are detected early in the development cycle.

#### Acceptance Criteria

1. WHEN code is pushed to the main branch, THE CI_Pipeline SHALL execute SonarCloud static code analysis after successful test execution
2. THE CI_Pipeline SHALL authenticate with SonarCloud using the SONAR_TOKEN secret stored in GitHub_Secrets
3. THE CI_Pipeline SHALL upload analysis results to the SonarCloud dashboard for the competence-backend1 project
4. WHEN SonarCloud analysis completes, THE CI_Pipeline SHALL make quality metrics visible including code smells, bugs, vulnerabilities, and technical debt
5. THE CI_Pipeline SHALL configure SonarCloud with the project key, organization, and source directories for accurate analysis
6. WHEN SonarCloud analysis fails due to authentication errors, THE CI_Pipeline SHALL log descriptive error messages indicating the authentication failure

### Requirement 2: Code Coverage Measurement and Reporting

**User Story:** As a developer, I want automated code coverage reporting, so that I can measure test effectiveness and identify untested code paths.

#### Acceptance Criteria

1. THE Maven_Build SHALL include the JaCoCo plugin configured to instrument bytecode during test execution
2. WHEN unit tests execute, THE JaCoCo_Agent SHALL collect coverage data for all executed code paths
3. WHEN tests complete, THE JaCoCo_Plugin SHALL generate coverage reports in XML and HTML formats
4. THE CI_Pipeline SHALL upload JaCoCo coverage data to SonarCloud for integration with quality metrics
5. WHEN coverage reports are generated, THE CI_Pipeline SHALL make coverage percentages visible for lines, branches, and methods
6. THE JaCoCo_Plugin SHALL execute the prepare-agent goal before tests and the report goal after tests complete
7. FOR ALL test executions, THE JaCoCo_Plugin SHALL produce coverage reports in the target/site/jacoco directory

### Requirement 3: Enhanced CI Pipeline Build Artifact

**User Story:** As a DevOps engineer, I want the CI pipeline to produce a deployable JAR artifact, so that the CD pipeline can package it into a Docker image.

#### Acceptance Criteria

1. WHEN all tests pass and code analysis completes, THE CI_Pipeline SHALL execute the Maven package goal to build the JAR_Artifact
2. THE CI_Pipeline SHALL upload the JAR_Artifact to GitHub Actions artifacts storage with the name "application-jar"
3. THE JAR_Artifact SHALL include all application dependencies and be executable as a standalone Spring Boot application
4. WHEN the Maven build executes, THE CI_Pipeline SHALL ensure JaCoCo instrumentation does not interfere with JAR packaging
5. THE CI_Pipeline SHALL retain the JAR_Artifact for 90 days to enable artifact retrieval and debugging

### Requirement 4: Docker Image Build and Packaging

**User Story:** As a DevOps engineer, I want automated Docker image creation, so that the application can be deployed in containerized environments.

#### Acceptance Criteria

1. WHEN the CI_Pipeline completes successfully, THE CD_Pipeline SHALL trigger automatically using the workflow_run event
2. THE CD_Pipeline SHALL download the JAR_Artifact produced by the CI_Pipeline from GitHub Actions artifacts storage
3. THE CD_Pipeline SHALL build a Docker_Image using the existing Dockerfile with the downloaded JAR_Artifact
4. THE Docker_Image SHALL be based on openjdk:21-jdk-slim and expose port 8089
5. THE CD_Pipeline SHALL tag the Docker_Image with both "latest" and the GitHub commit SHA for version tracking
6. WHEN the JAR_Artifact is not available, THE CD_Pipeline SHALL fail with a descriptive error message indicating the missing artifact

### Requirement 5: Docker Hub Publication

**User Story:** As a DevOps engineer, I want Docker images automatically pushed to Docker Hub, so that they are available for deployment to production and staging environments.

#### Acceptance Criteria

1. THE CD_Pipeline SHALL authenticate with Docker Hub using DOCKER_USERNAME and DOCKER_PASSWORD secrets stored in GitHub_Secrets
2. WHEN Docker image build completes successfully, THE CD_Pipeline SHALL push the Docker_Image to Docker Hub repository
3. THE CD_Pipeline SHALL push both the "latest" tag and the commit SHA tag to Docker Hub
4. WHEN Docker Hub authentication fails, THE CD_Pipeline SHALL fail with a descriptive error message indicating authentication failure
5. WHEN the Docker push completes, THE CD_Pipeline SHALL log the full image name and tags for verification
6. THE Docker_Image SHALL be publicly accessible on Docker Hub at the repository specified by DOCKER_USERNAME

### Requirement 6: Pipeline Orchestration and Automation

**User Story:** As a DevOps engineer, I want the CD pipeline to trigger automatically after successful CI completion, so that the deployment process is fully automated without manual intervention.

#### Acceptance Criteria

1. THE CD_Pipeline SHALL use the workflow_run trigger configured to activate on CI_Pipeline completion events
2. WHEN the CI_Pipeline completes with a "success" status, THE CD_Pipeline SHALL start execution automatically
3. WHEN the CI_Pipeline completes with a "failure" status, THE CD_Pipeline SHALL NOT trigger
4. THE CD_Pipeline SHALL execute only for the main branch to prevent deployment of feature branches
5. WHEN the CD_Pipeline triggers, THE CD_Pipeline SHALL have access to the same commit SHA and branch context as the CI_Pipeline
6. THE workflow_run trigger SHALL ensure the CD_Pipeline waits for CI_Pipeline completion before starting

### Requirement 7: Maven POM Configuration for Quality Tools

**User Story:** As a developer, I want the Maven build configuration to include JaCoCo and SonarCloud plugins, so that coverage and analysis execute as part of the standard build lifecycle.

#### Acceptance Criteria

1. THE Maven_POM SHALL include the JaCoCo Maven plugin version 0.8.11 or higher
2. THE JaCoCo_Plugin SHALL bind the prepare-agent goal to the initialize phase
3. THE JaCoCo_Plugin SHALL bind the report goal to the test phase
4. THE Maven_POM SHALL configure SonarCloud properties including sonar.projectKey, sonar.organization, and sonar.host.url
5. WHEN Maven executes the verify phase, THE Maven_Build SHALL generate both JaCoCo reports and execute SonarCloud analysis
6. THE Maven_POM SHALL exclude Lombok-generated code from coverage analysis to prevent inflated coverage metrics
7. THE JaCoCo_Plugin SHALL generate reports in both XML format for SonarCloud and HTML format for human review

### Requirement 8: GitHub Secrets Configuration

**User Story:** As a DevOps engineer, I want secure credential management for external services, so that sensitive authentication tokens are not exposed in workflow files or logs.

#### Acceptance Criteria

1. THE GitHub_Repository SHALL store the SONAR_TOKEN secret for SonarCloud authentication
2. THE GitHub_Repository SHALL store the DOCKER_USERNAME secret for Docker Hub authentication
3. THE GitHub_Repository SHALL store the DOCKER_PASSWORD secret for Docker Hub authentication
4. WHEN workflows reference secrets, THE GitHub_Actions SHALL inject secret values as environment variables without logging them
5. THE CI_Pipeline SHALL fail with a descriptive error when SONAR_TOKEN is missing or invalid
6. THE CD_Pipeline SHALL fail with a descriptive error when DOCKER_USERNAME or DOCKER_PASSWORD is missing or invalid
7. THE GitHub_Secrets SHALL be accessible only to workflows running on the main branch for security

### Requirement 9: SonarCloud Project Configuration

**User Story:** As a developer, I want SonarCloud properly configured for the competence-backend1 project, so that analysis results are accurate and accessible.

#### Acceptance Criteria

1. THE SonarCloud_Project SHALL be created with a unique project key matching the GitHub repository name
2. THE SonarCloud_Project SHALL be linked to the GitHub repository for automatic analysis triggering
3. THE SonarCloud_Configuration SHALL specify Java 21 as the source language version
4. THE SonarCloud_Configuration SHALL analyze source code in the src/main/java directory
5. THE SonarCloud_Configuration SHALL import JaCoCo coverage reports from target/site/jacoco/jacoco.xml
6. THE SonarCloud_Configuration SHALL exclude test files in src/test/java from coverage requirements
7. WHEN analysis completes, THE SonarCloud_Dashboard SHALL display quality gate status, coverage percentage, code smells, bugs, and vulnerabilities

### Requirement 10: CI Pipeline Quality Gates

**User Story:** As a team lead, I want the CI pipeline to enforce quality standards, so that low-quality code does not proceed to deployment.

#### Acceptance Criteria

1. WHEN SonarCloud analysis completes, THE CI_Pipeline SHALL evaluate the Quality_Gate status
2. THE CI_Pipeline SHALL continue to artifact building when the Quality_Gate passes
3. THE CI_Pipeline SHALL mark the workflow as successful even if Quality_Gate fails, to allow CD_Pipeline triggering for demonstration purposes
4. THE CI_Pipeline SHALL log Quality_Gate status prominently in workflow output for visibility
5. WHEN code coverage falls below the configured threshold, THE SonarCloud_Quality_Gate SHALL report the coverage failure
6. THE Quality_Gate SHALL evaluate conditions for new code coverage, maintainability rating, reliability rating, and security rating

### Requirement 11: CD Pipeline Conditional Execution

**User Story:** As a DevOps engineer, I want the CD pipeline to execute only when CI succeeds, so that broken builds are not deployed.

#### Acceptance Criteria

1. THE CD_Pipeline SHALL check the conclusion status of the triggering CI_Pipeline workflow run
2. WHEN the CI_Pipeline conclusion is "success", THE CD_Pipeline SHALL proceed with Docker image build
3. WHEN the CI_Pipeline conclusion is "failure", "cancelled", or "skipped", THE CD_Pipeline SHALL exit immediately without building images
4. THE CD_Pipeline SHALL log the CI_Pipeline conclusion status for debugging and audit purposes
5. THE CD_Pipeline SHALL use the same commit SHA as the CI_Pipeline to ensure version consistency
6. WHEN multiple CI_Pipeline runs complete simultaneously, THE CD_Pipeline SHALL process each run independently

### Requirement 12: Workflow Logging and Observability

**User Story:** As a developer, I want comprehensive logging in CI/CD workflows, so that I can debug failures and understand pipeline execution.

#### Acceptance Criteria

1. THE CI_Pipeline SHALL log the start and completion of each major step including checkout, build, test, coverage, and analysis
2. THE CI_Pipeline SHALL log SonarCloud analysis URL for direct access to quality reports
3. THE CD_Pipeline SHALL log Docker image names and tags after successful build
4. THE CD_Pipeline SHALL log Docker Hub push status and repository URL
5. WHEN any step fails, THE Pipeline SHALL log error messages with sufficient context for debugging
6. THE CI_Pipeline SHALL log JaCoCo coverage summary including line coverage percentage
7. THE CD_Pipeline SHALL log the source CI_Pipeline run ID and commit SHA for traceability

### Requirement 13: MySQL Database Integration for Testing

**User Story:** As a developer, I want MySQL database available during CI testing, so that integration tests execute against a real database.

#### Acceptance Criteria

1. THE CI_Pipeline SHALL start a MySQL 8.0 service container before test execution
2. THE MySQL_Service SHALL create the Competence database automatically using the MYSQL_DATABASE environment variable
3. THE CI_Pipeline SHALL configure health checks to ensure MySQL is ready before tests start
4. THE CI_Pipeline SHALL wait 15 seconds after MySQL health checks pass to ensure full database initialization
5. THE CI_Pipeline SHALL provide database connection parameters to tests via environment variables SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, and SPRING_DATASOURCE_PASSWORD
6. THE MySQL_Service SHALL use root password "root" matching the test configuration
7. WHEN MySQL fails to start, THE CI_Pipeline SHALL fail with health check timeout errors

### Requirement 14: Documentation and Setup Instructions

**User Story:** As a new team member, I want clear documentation for setting up the CI/CD pipeline, so that I can configure secrets and understand the workflow.

#### Acceptance Criteria

1. THE Documentation SHALL provide step-by-step instructions for creating a SonarCloud account and project
2. THE Documentation SHALL explain how to generate and configure the SONAR_TOKEN in GitHub Secrets
3. THE Documentation SHALL provide instructions for creating a Docker Hub account and generating access tokens
4. THE Documentation SHALL explain how to configure DOCKER_USERNAME and DOCKER_PASSWORD in GitHub Secrets
5. THE Documentation SHALL describe the complete CI/CD workflow flow from code push to Docker Hub publication
6. THE Documentation SHALL include troubleshooting guidance for common failure scenarios including authentication errors and missing artifacts
7. THE Documentation SHALL explain how to view SonarCloud reports and JaCoCo coverage reports

### Requirement 15: Workflow File Naming and Organization

**User Story:** As a DevOps engineer, I want clearly named and organized workflow files, so that the pipeline structure is easy to understand and maintain.

#### Acceptance Criteria

1. THE CI_Pipeline SHALL be defined in the file .github/workflows/ci.yml
2. THE CD_Pipeline SHALL be defined in the file .github/workflows/cd.yml
3. THE CI_Pipeline workflow SHALL be named "CI Backend" for clear identification in GitHub Actions UI
4. THE CD_Pipeline workflow SHALL be named "CD Backend" for clear identification in GitHub Actions UI
5. THE Workflow_Files SHALL include comments explaining the purpose of each major step
6. THE Workflow_Files SHALL use consistent indentation and YAML formatting for readability
7. THE Workflow_Files SHALL group related steps logically with descriptive step names

