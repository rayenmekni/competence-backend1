# Implementation Plan: Complete CI/CD Pipeline

## Overview

This implementation plan converts the CI/CD pipeline design into actionable coding tasks. The plan follows a sequential approach: first fixing the Maven configuration foundation, then enhancing the CI pipeline with quality tools, creating the CD pipeline, and finally adding documentation. Each task builds on previous work to ensure incremental validation and integration.

## Tasks

- [x] 1. Fix Maven POM configuration and add quality tool plugins
  - [x] 1.1 Fix duplicate `<build>` tags in pom.xml
    - Remove the duplicate `<build>` section at the end of the file
    - Merge JaCoCo plugin configuration into the existing `<build>` section
    - Ensure valid XML structure with single `<build>` element
    - _Requirements: 7.1, 7.2, 7.3_
  
  - [x] 1.2 Add SonarCloud properties to pom.xml
    - Add `<properties>` section with sonar.projectKey, sonar.organization, sonar.host.url
    - Configure sonar.sources to point to src/main/java
    - Configure sonar.tests to point to src/test/java
    - Configure sonar.java.source to 21
    - Configure sonar.coverage.jacoco.xmlReportPaths to target/site/jacoco/jacoco.xml
    - _Requirements: 7.4, 9.3, 9.4, 9.5_
  
  - [x] 1.3 Configure JaCoCo plugin with Lombok exclusions
    - Ensure JaCoCo plugin version 0.8.11 or higher
    - Configure prepare-agent goal bound to initialize phase
    - Configure report goal bound to test phase
    - Add configuration to generate both XML and HTML reports
    - _Requirements: 7.1, 7.2, 7.3, 7.7, 2.6_
  
  - [x] 1.4 Create lombok.config file for coverage exclusion
    - Create lombok.config in project root
    - Add configuration: `lombok.addLombokGeneratedAnnotation = true`
    - This instructs Lombok to add @lombok.Generated annotations that JaCoCo excludes
    - _Requirements: 7.6, 2.2_

- [x] 2. Enhance CI pipeline with code quality and coverage
  - [x] 2.1 Add JaCoCo report generation step to ci.yml
    - Add step after "Run tests" to generate JaCoCo reports
    - Execute: `./mvnw jacoco:report`
    - Add step name: "Generate JaCoCo coverage report"
    - _Requirements: 2.3, 2.6, 2.7, 12.6_
  
  - [x] 2.2 Add SonarCloud analysis step to ci.yml
    - Add step after JaCoCo report generation
    - Use SonarSource/sonarcloud-github-action@master
    - Configure environment variable SONAR_TOKEN from GitHub secrets
    - Add step name: "SonarCloud analysis"
    - Log SonarCloud dashboard URL for visibility
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 12.2_
  
  - [x] 2.3 Add Maven package step to ci.yml
    - Add step after SonarCloud analysis
    - Execute: `./mvnw package -DskipTests`
    - Skip tests since they already ran in previous step
    - Add step name: "Build JAR artifact"
    - _Requirements: 3.1, 3.3, 3.4_
  
  - [x] 2.4 Add artifact upload step to ci.yml
    - Add step after Maven package
    - Use actions/upload-artifact@v4
    - Upload target/*.jar with artifact name "application-jar"
    - Set retention-days to 90
    - Add step name: "Upload JAR artifact"
    - _Requirements: 3.2, 3.5, 12.1_

- [ ] 3. Checkpoint - Verify CI pipeline enhancements
  - Run the enhanced CI pipeline locally or push to trigger GitHub Actions
  - Verify JaCoCo reports are generated in target/site/jacoco/
  - Verify JAR artifact is created in target/
  - Ensure all tests pass, ask the user if questions arise

- [x] 4. Create CD pipeline for Docker deployment
  - [x] 4.1 Create .github/workflows/cd.yml file
    - Create new file with workflow name "CD Backend"
    - Configure workflow_run trigger for "CI Backend" workflow
    - Set trigger types to [completed]
    - Configure to run only on main branch
    - Add conditional check: `if: github.event.workflow_run.conclusion == 'success'`
    - _Requirements: 4.1, 6.1, 6.2, 6.3, 6.4, 11.1, 11.2, 11.3_
  
  - [x] 4.2 Add checkout and artifact download steps to cd.yml
    - Add checkout step using actions/checkout@v4
    - Add artifact download step using actions/download-artifact@v4
    - Download artifact named "application-jar" to target/ directory
    - Add error handling for missing artifact
    - Add step names: "Checkout code" and "Download JAR artifact"
    - _Requirements: 4.2, 4.6, 11.5, 12.7_
  
  - [x] 4.3 Add Docker Hub login step to cd.yml
    - Add Docker login step using docker/login-action@v3
    - Configure username from secrets.DOCKER_USERNAME
    - Configure password from secrets.DOCKER_PASSWORD
    - Add step name: "Login to Docker Hub"
    - _Requirements: 5.1, 5.4, 8.2, 8.3, 8.6_
  
  - [x] 4.4 Add Docker build and push steps to cd.yml
    - Add Docker build step using docker/build-push-action@v5
    - Configure context to current directory
    - Configure file to ./Dockerfile
    - Enable push: true
    - Configure tags: both latest and commit SHA (${{ github.sha }})
    - Add step name: "Build and push Docker image"
    - Log image name and tags after successful push
    - _Requirements: 4.3, 4.4, 4.5, 5.2, 5.3, 5.5, 12.3, 12.4_

- [ ] 5. Checkpoint - Verify CD pipeline creation
  - Review cd.yml for correct syntax and structure
  - Verify workflow_run trigger configuration
  - Verify Docker build configuration references correct Dockerfile
  - Ensure all tests pass, ask the user if questions arise

- [x] 6. Create documentation for secrets and setup
  - [x] 6.1 Create CICD_SETUP.md documentation file
    - Create new file in project root
    - Add title: "CI/CD Pipeline Setup Guide"
    - Add overview section explaining the complete pipeline flow
    - _Requirements: 14.5, 15.5_
  
  - [x] 6.2 Add SonarCloud setup instructions to CICD_SETUP.md
    - Document how to create SonarCloud account at sonarcloud.io
    - Document how to create new project and link to GitHub repository
    - Document how to generate SONAR_TOKEN from SonarCloud account settings
    - Document how to add SONAR_TOKEN to GitHub repository secrets
    - Include project key and organization configuration
    - _Requirements: 14.1, 14.2, 8.1, 8.5_
  
  - [x] 6.3 Add Docker Hub setup instructions to CICD_SETUP.md
    - Document how to create Docker Hub account at hub.docker.com
    - Document how to generate access token from Docker Hub account settings
    - Document how to add DOCKER_USERNAME to GitHub repository secrets
    - Document how to add DOCKER_PASSWORD (access token) to GitHub repository secrets
    - _Requirements: 14.3, 14.4, 8.2, 8.3, 8.6_
  
  - [x] 6.4 Add troubleshooting section to CICD_SETUP.md
    - Document common failure scenarios and solutions
    - Include authentication error troubleshooting for SonarCloud
    - Include authentication error troubleshooting for Docker Hub
    - Include missing artifact troubleshooting for CD pipeline
    - Include MySQL connection troubleshooting for CI tests
    - Document how to view SonarCloud reports and JaCoCo coverage reports
    - _Requirements: 14.6, 14.7, 1.6, 5.4, 4.6, 13.7_
  
  - [x] 6.5 Add workflow execution guide to CICD_SETUP.md
    - Document the complete workflow from code push to Docker Hub publication
    - Explain CI pipeline steps: checkout, test, coverage, analysis, package, upload
    - Explain CD pipeline steps: trigger, download, build, push
    - Document how to view workflow runs in GitHub Actions UI
    - Document how to access SonarCloud dashboard and coverage reports
    - _Requirements: 14.5, 12.1, 12.2, 12.3, 12.4, 12.5, 12.6, 12.7_

- [ ] 7. Final checkpoint - Complete pipeline validation
  - Review all modified files: pom.xml, ci.yml, cd.yml, lombok.config, CICD_SETUP.md
  - Verify XML structure is valid in pom.xml
  - Verify YAML syntax is valid in workflow files
  - Verify all requirements are addressed in implementation
  - Ensure all tests pass, ask the user if questions arise

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each task references specific requirements for traceability
- Checkpoints ensure incremental validation
- This is a DevOps/Infrastructure feature - no property-based tests are applicable
- The implementation focuses on configuration files (XML, YAML) and documentation
- Secrets (SONAR_TOKEN, DOCKER_USERNAME, DOCKER_PASSWORD) must be configured manually in GitHub repository settings
- The CD pipeline will only trigger after successful CI completion on the main branch
