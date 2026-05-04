# Requirements Document: Complete CI/CD Pipeline

## Introduction

This document specifies the requirements for completing the DevOps CI/CD pipeline for the competence-backend1 Spring Boot microservice. The system will enhance the existing CI pipeline with code quality analysis and coverage reporting, implement a CD pipeline for Docker image deployment, and automate the pipeline orchestration to meet academic requirements for DevOps coursework.

The current CI pipeline successfully executes unit tests with MySQL integration. This feature will add mandatory static code analysis with SonarQube, code coverage reporting with JaCoCo, automated Docker image building and publishing to Docker Hub, and automatic triggering of the CD pipeline upon successful CI completion using Jenkins pipeline stages.

## Glossary

- **Jenkins_Pipeline**: The automated CI/CD pipeline defined in a Jenkinsfile that orchestrates build, test, analysis, and deployment stages
- **CI_Stage**: The Continuous Integration stages in the Jenkins pipeline that build, test, and analyze code quality
- **CD_Stage**: The Continuous Deployment stages in the Jenkins pipeline that build Docker images and publish them to Docker Hub
- **SonarQube**: Static code analysis platform that evaluates code quality, security vulnerabilities, and technical debt
- **JaCoCo**: Java Code Coverage library that measures and reports test coverage metrics
- **Coverage_Report**: A document showing the percentage of code executed by automated tests
- **Docker_Image**: A packaged, executable artifact containing the application and its runtime dependencies
- **Docker_Hub**: A cloud-based registry service for storing and distributing Docker images
- **Jenkinsfile**: A text file containing the definition of a Jenkins pipeline written in Groovy DSL
- **Maven**: The build automation tool used for compiling, testing, and packaging the Spring Boot application
- **Pipeline_Stage**: A logical division of work in a Jenkins pipeline (e.g., Build, Test, Analysis, Deploy)
- **Quality_Gate**: A set of threshold conditions in SonarQube that must be met for code to be considered acceptable
- **JAR_Artifact**: The compiled Java application packaged as a Java Archive file
- **Jenkins_Credentials**: Encrypted credentials stored in Jenkins for secure access to external services
- **Workspace_Artifact**: Build artifacts stored in the Jenkins workspace and passed between pipeline stages
- **Post_Actions**: Jenkins pipeline blocks that execute after stages complete (success, failure, always)

## Requirements

### Requirement 1: Static Code Analysis Integration

**User Story:** As a developer, I want automated static code analysis on every code push, so that code quality issues and security vulnerabilities are detected early in the development cycle.

#### Acceptance Criteria

1. WHEN code is pushed to the main branch, THE Jenkins_Pipeline SHALL execute SonarQube static code analysis in a dedicated stage after successful test execution
2. THE Jenkins_Pipeline SHALL authenticate with SonarQube using credentials stored in Jenkins_Credentials
3. THE Jenkins_Pipeline SHALL upload analysis results to the SonarQube server for the competence-backend1 project
4. WHEN SonarQube analysis completes, THE Jenkins_Pipeline SHALL make quality metrics visible in the console output including code smells, bugs, vulnerabilities, and technical debt
5. THE Jenkins_Pipeline SHALL configure SonarQube analysis with the project key and source directories for accurate analysis
6. WHEN SonarQube analysis fails due to authentication errors, THE Jenkins_Pipeline SHALL log descriptive error messages indicating the authentication failure

### Requirement 2: Code Coverage Measurement and Reporting

**User Story:** As a developer, I want automated code coverage reporting, so that I can measure test effectiveness and identify untested code paths.

#### Acceptance Criteria

1. THE Maven_Build SHALL include the JaCoCo plugin configured to instrument bytecode during test execution
2. WHEN unit tests execute in the Jenkins pipeline, THE JaCoCo_Agent SHALL collect coverage data for all executed code paths
3. WHEN tests complete, THE JaCoCo_Plugin SHALL generate coverage reports in XML and HTML formats
4. THE Jenkins_Pipeline SHALL upload JaCoCo coverage data to SonarQube for integration with quality metrics
5. WHEN coverage reports are generated, THE Jenkins_Pipeline SHALL make coverage percentages visible in console output for lines, branches, and methods
6. THE JaCoCo_Plugin SHALL execute the prepare-agent goal before tests and the report goal after tests complete
7. FOR ALL test executions, THE JaCoCo_Plugin SHALL produce coverage reports in the target/site/jacoco directory

### Requirement 3: Enhanced CI Pipeline Build Artifact

**User Story:** As a DevOps engineer, I want the CI pipeline to produce a deployable JAR artifact, so that the CD pipeline can package it into a Docker image.

#### Acceptance Criteria

1. WHEN all tests pass and code analysis completes, THE Jenkins_Pipeline SHALL execute the Maven package goal to build the JAR_Artifact
2. THE Jenkins_Pipeline SHALL preserve the JAR_Artifact in the Jenkins workspace for use by subsequent CD stages
3. THE JAR_Artifact SHALL include all application dependencies and be executable as a standalone Spring Boot application
4. WHEN the Maven build executes, THE Jenkins_Pipeline SHALL ensure JaCoCo instrumentation does not interfere with JAR packaging
5. THE Jenkins_Pipeline SHALL use the stash/unstash mechanism to pass the JAR_Artifact between pipeline stages

### Requirement 4: Docker Image Build and Packaging

**User Story:** As a DevOps engineer, I want automated Docker image creation, so that the application can be deployed in containerized environments.

#### Acceptance Criteria

1. WHEN the CI stages complete successfully, THE Jenkins_Pipeline SHALL proceed to the Docker build stage
2. THE Jenkins_Pipeline SHALL use the JAR_Artifact from the previous stage to build the Docker_Image
3. THE Jenkins_Pipeline SHALL build a Docker_Image using the existing Dockerfile with the JAR_Artifact
4. THE Docker_Image SHALL be based on eclipse-temurin:21-jre-alpine and expose port 8089
5. THE Jenkins_Pipeline SHALL tag the Docker_Image with both "latest" and the Git commit SHA for version tracking
6. WHEN the JAR_Artifact is not available, THE Jenkins_Pipeline SHALL fail with a descriptive error message indicating the missing artifact

### Requirement 5: Docker Hub Publication

**User Story:** As a DevOps engineer, I want Docker images automatically pushed to Docker Hub, so that they are available for deployment to production and staging environments.

#### Acceptance Criteria

1. THE Jenkins_Pipeline SHALL authenticate with Docker Hub using credentials stored in Jenkins_Credentials
2. WHEN Docker image build completes successfully, THE Jenkins_Pipeline SHALL push the Docker_Image to Docker Hub repository
3. THE Jenkins_Pipeline SHALL push both the "latest" tag and the commit SHA tag to Docker Hub
4. WHEN Docker Hub authentication fails, THE Jenkins_Pipeline SHALL fail with a descriptive error message indicating authentication failure
5. WHEN the Docker push completes, THE Jenkins_Pipeline SHALL log the full image name and tags for verification in console output
6. THE Docker_Image SHALL be publicly accessible on Docker Hub at the repository specified by the Docker Hub username

### Requirement 6: Pipeline Orchestration and Automation

**User Story:** As a DevOps engineer, I want the CD stages to execute automatically after successful CI completion, so that the deployment process is fully automated without manual intervention.

#### Acceptance Criteria

1. THE Jenkins_Pipeline SHALL define CI and CD stages in a single Jenkinsfile with sequential execution
2. WHEN the CI stages (Build, Test, Analysis) complete with "success" status, THE Jenkins_Pipeline SHALL proceed to CD stages automatically
3. WHEN any CI stage fails, THE Jenkins_Pipeline SHALL skip CD stages and mark the pipeline as failed
4. THE Jenkins_Pipeline SHALL execute only for the main branch to prevent deployment of feature branches
5. THE Jenkins_Pipeline SHALL use the same Git commit SHA throughout all stages for version consistency
6. THE Jenkins_Pipeline SHALL use conditional stage execution with "when" directives to control CD stage execution based on CI success

### Requirement 7: Maven POM Configuration for Quality Tools

**User Story:** As a developer, I want the Maven build configuration to include JaCoCo and SonarQube plugins, so that coverage and analysis execute as part of the standard build lifecycle.

#### Acceptance Criteria

1. THE Maven_POM SHALL include the JaCoCo Maven plugin version 0.8.11 or higher
2. THE JaCoCo_Plugin SHALL bind the prepare-agent goal to the initialize phase
3. THE JaCoCo_Plugin SHALL bind the report goal to the test phase
4. THE Maven_POM SHALL configure SonarQube properties including sonar.projectKey and sonar.host.url
5. WHEN Maven executes the verify phase, THE Maven_Build SHALL generate both JaCoCo reports and execute SonarQube analysis
6. THE Maven_POM SHALL exclude Lombok-generated code from coverage analysis to prevent inflated coverage metrics
7. THE JaCoCo_Plugin SHALL generate reports in both XML format for SonarQube and HTML format for human review

### Requirement 8: Jenkins Credentials Configuration

**User Story:** As a DevOps engineer, I want secure credential management for external services, so that sensitive authentication tokens are not exposed in the Jenkinsfile or console logs.

#### Acceptance Criteria

1. THE Jenkins_Server SHALL store the SONAR_TOKEN credential for SonarQube authentication
2. THE Jenkins_Server SHALL store the DOCKER_USERNAME credential for Docker Hub authentication
3. THE Jenkins_Server SHALL store the DOCKER_PASSWORD credential for Docker Hub authentication
4. WHEN the pipeline references credentials, THE Jenkins_Pipeline SHALL inject credential values as environment variables without logging them
5. THE Jenkins_Pipeline SHALL fail with a descriptive error when SONAR_TOKEN is missing or invalid
6. THE Jenkins_Pipeline SHALL fail with a descriptive error when DOCKER_USERNAME or DOCKER_PASSWORD is missing or invalid
7. THE Jenkins_Credentials SHALL be configured as "Secret text" or "Username with password" credential types for secure storage

### Requirement 9: SonarQube Project Configuration

**User Story:** As a developer, I want SonarQube properly configured for the competence-backend1 project, so that analysis results are accurate and accessible.

#### Acceptance Criteria

1. THE SonarQube_Project SHALL be created with a unique project key matching the repository name
2. THE SonarQube_Project SHALL be accessible from the Jenkins pipeline for automated analysis
3. THE SonarQube_Configuration SHALL specify Java 21 as the source language version
4. THE SonarQube_Configuration SHALL analyze source code in the src/main/java directory
5. THE SonarQube_Configuration SHALL import JaCoCo coverage reports from target/site/jacoco/jacoco.xml
6. THE SonarQube_Configuration SHALL exclude test files in src/test/java from coverage requirements
7. WHEN analysis completes, THE SonarQube_Dashboard SHALL display quality gate status, coverage percentage, code smells, bugs, and vulnerabilities

### Requirement 10: CI Pipeline Quality Gates

**User Story:** As a team lead, I want the CI pipeline to enforce quality standards, so that low-quality code does not proceed to deployment.

#### Acceptance Criteria

1. WHEN SonarQube analysis completes, THE Jenkins_Pipeline SHALL evaluate the Quality_Gate status
2. THE Jenkins_Pipeline SHALL continue to Docker build stages when the Quality_Gate passes
3. THE Jenkins_Pipeline SHALL mark the pipeline as successful even if Quality_Gate fails, to allow CD stages for demonstration purposes
4. THE Jenkins_Pipeline SHALL log Quality_Gate status prominently in console output for visibility
5. WHEN code coverage falls below the configured threshold, THE SonarQube_Quality_Gate SHALL report the coverage failure
6. THE Quality_Gate SHALL evaluate conditions for new code coverage, maintainability rating, reliability rating, and security rating

### Requirement 11: CD Pipeline Conditional Execution

**User Story:** As a DevOps engineer, I want the CD stages to execute only when CI succeeds, so that broken builds are not deployed.

#### Acceptance Criteria

1. THE Jenkins_Pipeline SHALL use conditional "when" directives to control CD stage execution
2. WHEN all CI stages complete successfully, THE Jenkins_Pipeline SHALL proceed with Docker image build
3. WHEN any CI stage fails, THE Jenkins_Pipeline SHALL skip CD stages and mark the pipeline as failed
4. THE Jenkins_Pipeline SHALL log the CI stage status before attempting CD stages for debugging and audit purposes
5. THE Jenkins_Pipeline SHALL use the same workspace and Git commit SHA for both CI and CD stages to ensure version consistency
6. THE Jenkins_Pipeline SHALL execute CD stages only on the main branch using branch conditions

### Requirement 12: Pipeline Logging and Observability

**User Story:** As a developer, I want comprehensive logging in the Jenkins pipeline, so that I can debug failures and understand pipeline execution.

#### Acceptance Criteria

1. THE Jenkins_Pipeline SHALL log the start and completion of each stage including Build, Test, Coverage, Analysis, Docker Build, and Docker Push
2. THE Jenkins_Pipeline SHALL log SonarQube analysis URL for direct access to quality reports
3. THE Jenkins_Pipeline SHALL log Docker image names and tags after successful build
4. THE Jenkins_Pipeline SHALL log Docker Hub push status and repository URL
5. WHEN any stage fails, THE Jenkins_Pipeline SHALL log error messages with sufficient context for debugging
6. THE Jenkins_Pipeline SHALL log JaCoCo coverage summary including line coverage percentage
7. THE Jenkins_Pipeline SHALL log the Git commit SHA and branch name for traceability

### Requirement 13: MySQL Database Integration for Testing

**User Story:** As a developer, I want MySQL database available during CI testing, so that integration tests execute against a real database.

#### Acceptance Criteria

1. THE Jenkins_Pipeline SHALL start a MySQL 8.0 Docker container before test execution
2. THE MySQL_Container SHALL create the Competence database automatically using environment variables
3. THE Jenkins_Pipeline SHALL configure health checks or wait conditions to ensure MySQL is ready before tests start
4. THE Jenkins_Pipeline SHALL wait for MySQL initialization to complete before executing tests
5. THE Jenkins_Pipeline SHALL provide database connection parameters to tests via environment variables SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, and SPRING_DATASOURCE_PASSWORD
6. THE MySQL_Container SHALL use root password "root" matching the test configuration
7. WHEN MySQL fails to start, THE Jenkins_Pipeline SHALL fail with descriptive error messages

### Requirement 14: Documentation and Setup Instructions

**User Story:** As a new team member, I want clear documentation for setting up the CI/CD pipeline, so that I can configure credentials and understand the workflow.

#### Acceptance Criteria

1. THE Documentation SHALL provide step-by-step instructions for setting up a SonarQube server or using SonarCloud
2. THE Documentation SHALL explain how to generate and configure the SONAR_TOKEN in Jenkins_Credentials
3. THE Documentation SHALL provide instructions for creating a Docker Hub account and generating access tokens
4. THE Documentation SHALL explain how to configure DOCKER_USERNAME and DOCKER_PASSWORD in Jenkins_Credentials
5. THE Documentation SHALL describe the complete CI/CD pipeline flow from code push to Docker Hub publication
6. THE Documentation SHALL include troubleshooting guidance for common failure scenarios including authentication errors and missing artifacts
7. THE Documentation SHALL explain how to view SonarQube reports and JaCoCo coverage reports

### Requirement 15: Jenkinsfile Organization and Structure

**User Story:** As a DevOps engineer, I want a clearly structured Jenkinsfile, so that the pipeline is easy to understand and maintain.

#### Acceptance Criteria

1. THE Jenkins_Pipeline SHALL be defined in a file named Jenkinsfile in the project root directory
2. THE Jenkinsfile SHALL use declarative pipeline syntax for clarity and maintainability
3. THE Jenkinsfile SHALL define distinct stages for Build, Test, Coverage, SonarQube Analysis, Package, Docker Build, and Docker Push
4. THE Jenkinsfile SHALL include comments explaining the purpose of each stage
5. THE Jenkinsfile SHALL use consistent indentation and formatting for readability
6. THE Jenkinsfile SHALL group related steps logically within each stage
7. THE Jenkinsfile SHALL use environment blocks to define credentials and configuration variables

