# Implementation Plan: Complete CI/CD Pipeline

## Overview

This implementation plan converts the CI/CD pipeline design into actionable coding tasks. The plan follows a sequential approach: first fixing the Maven configuration foundation, then creating the Jenkins pipeline with CI and CD stages, and finally adding documentation. Each task builds on previous work to ensure incremental validation and integration.

## Tasks

- [x] 1. Fix Maven POM configuration and add quality tool plugins
  - [x] 1.1 Fix duplicate `<build>` tags in pom.xml
    - Remove the duplicate `<build>` section at the end of the file
    - Merge JaCoCo plugin configuration into the existing `<build>` section
    - Ensure valid XML structure with single `<build>` element
    - _Requirements: 7.1, 7.2, 7.3_
  
  - [x] 1.2 Add SonarQube properties to pom.xml
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

- [ ] 2. Create Jenkinsfile with CI stages
  - [ ] 2.1 Create Jenkinsfile with pipeline structure
    - Create new file named "Jenkinsfile" in project root
    - Use declarative pipeline syntax
    - Configure agent with label 'docker-maven-java21' or 'any'
    - Configure tools block with Maven-3.9 and JDK-21
    - Add environment block with credentials and configuration variables
    - _Requirements: 15.1, 15.2, 15.3, 15.4, 15.5, 15.6_
  
  - [ ] 2.2 Add Checkout and Build stages to Jenkinsfile
    - Add Checkout stage with 'checkout scm' step
    - Log Git branch and commit SHA for traceability
    - Add Build stage with 'mvn clean compile' command
    - Add echo statements for stage visibility
    - _Requirements: 12.7, 12.1_
  
  - [ ] 2.3 Add MySQL container stage to Jenkinsfile
    - Add "Start MySQL" stage before Test stage
    - Use docker run command to start MySQL 8.0 container
    - Configure container name using BUILD_ID for uniqueness
    - Set environment variables: MYSQL_ROOT_PASSWORD=root, MYSQL_DATABASE=Competence
    - Map port 3307:3306 for host access
    - Add health check with timeout using mysqladmin ping
    - Add error handling for MySQL startup failures
    - _Requirements: 13.1, 13.2, 13.3, 13.4, 13.5, 13.6, 13.7_
  
  - [ ] 2.4 Add Test and Coverage stages to Jenkinsfile
    - Add Test stage with 'mvn test' command
    - Add post-always block to publish JUnit test results
    - Add Coverage Report stage with 'mvn jacoco:report' command
    - Log coverage report location
    - _Requirements: 2.1, 2.2, 2.3, 2.6, 2.7, 12.6_
  
  - [ ] 2.5 Add SonarQube Analysis stage to Jenkinsfile
    - Add SonarQube Analysis stage with withSonarQubeEnv wrapper
    - Execute 'mvn sonar:sonar' with project key and token
    - Add error handling for authentication failures
    - Log SonarQube dashboard URL
    - _Requirements: 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 12.2_
  
  - [ ] 2.6 Add Quality Gate stage to Jenkinsfile
    - Add Quality Gate stage with waitForQualityGate function
    - Set timeout to 5 minutes
    - Log quality gate status and conditions
    - Continue pipeline even if quality gate fails (for demonstration)
    - _Requirements: 10.1, 10.2, 10.3, 10.4, 10.5, 10.6_
  
  - [ ] 2.7 Add Package stage to Jenkinsfile
    - Add Package stage with 'mvn package -DskipTests' command
    - Use stash to preserve JAR artifact for Docker build
    - Stash target/*.jar with name 'jar-artifact'
    - Log stash operation success
    - _Requirements: 3.1, 3.2, 3.3, 3.4, 3.5_

- [ ] 3. Checkpoint - Verify CI stages in Jenkinsfile
  - Review Jenkinsfile for correct syntax and structure
  - Verify all CI stages are defined: Checkout, Build, Start MySQL, Test, Coverage, Analysis, Quality Gate, Package
  - Verify environment variables for MySQL connection
  - Ensure all tests pass, ask the user if questions arise

- [ ] 4. Add CD stages to Jenkinsfile
  - [ ] 4.1 Add Docker Build stage to Jenkinsfile
    - Add Docker Build stage with conditional execution: when { branch 'main' }
    - Use unstash to retrieve JAR artifact from Package stage
    - Add error handling for missing artifact
    - Execute docker build command with multiple tags: latest and commit SHA
    - Use ${DOCKER_IMAGE}:latest and ${DOCKER_IMAGE}:${env.GIT_COMMIT}
    - Log Docker image names and tags
    - _Requirements: 4.1, 4.2, 4.3, 4.4, 4.5, 4.6, 11.1, 11.2, 11.3, 11.4, 11.5, 11.6, 12.3_
  
  - [ ] 4.2 Add Docker Push stage to Jenkinsfile
    - Add Docker Push stage with conditional execution: when { branch 'main' }
    - Add Docker Hub login using credentials
    - Use echo and docker login with --password-stdin
    - Add error handling for authentication failures
    - Execute docker push for both latest and commit SHA tags
    - Log Docker Hub repository URL and pushed tags
    - _Requirements: 5.1, 5.2, 5.3, 5.4, 5.5, 5.6, 12.4, 12.5_
  
  - [ ] 4.3 Add post-build actions to Jenkinsfile
    - Add post block with always, success, and failure sections
    - In always block: stop and remove MySQL container
    - Use 'docker stop' and 'docker rm' with '|| true' for error tolerance
    - In success block: log pipeline completion message
    - In failure block: log pipeline failure message
    - _Requirements: 6.5, 12.1, 12.5_

- [ ] 5. Checkpoint - Verify complete Jenkinsfile
  - Review Jenkinsfile for all stages: CI and CD
  - Verify conditional execution for CD stages (main branch only)
  - Verify stash/unstash for artifact management
  - Verify post-build cleanup actions
  - Ensure all tests pass, ask the user if questions arise

- [ ] 6. Delete GitHub Actions workflow files
  - [ ] 6.1 Delete .github/workflows/ci.yml file
    - Remove the GitHub Actions CI workflow file
    - This is replaced by Jenkins pipeline CI stages
    - _Requirements: 15.1_
  
  - [ ] 6.2 Delete .github/workflows/cd.yml file
    - Remove the GitHub Actions CD workflow file
    - This is replaced by Jenkins pipeline CD stages
    - _Requirements: 15.1_

- [ ] 7. Update documentation for Jenkins setup
  - [ ] 7.1 Update CICD_SETUP.md for Jenkins
    - Update title to reflect Jenkins pipeline
    - Replace GitHub Actions references with Jenkins references
    - Update overview section to explain Jenkins pipeline flow
    - _Requirements: 14.5, 15.5_
  
  - [ ] 7.2 Add Jenkins configuration instructions to CICD_SETUP.md
    - Document Jenkins Global Tool Configuration for Maven and JDK
    - Document Jenkins agent requirements (Docker, Maven, Java 21)
    - Document SonarQube server configuration in Jenkins
    - Document Jenkins credentials setup for sonar-token and docker-credentials
    - Include step-by-step instructions with Jenkins UI paths
    - _Requirements: 8.1, 8.2, 8.3, 8.4, 8.5, 8.6, 8.7_
  
  - [ ] 7.3 Update SonarQube setup instructions in CICD_SETUP.md
    - Keep SonarQube account creation instructions
    - Update credential configuration to use Jenkins Credentials store
    - Document how to add SONAR_TOKEN as Secret text in Jenkins
    - Document SonarQube webhook configuration to Jenkins
    - _Requirements: 14.1, 14.2, 8.1, 8.5_
  
  - [ ] 7.4 Update Docker Hub setup instructions in CICD_SETUP.md
    - Keep Docker Hub account creation instructions
    - Update credential configuration to use Jenkins Credentials store
    - Document how to add docker-credentials as Username with password in Jenkins
    - Explain _USR and _PSW suffix usage in Jenkins
    - _Requirements: 14.3, 14.4, 8.2, 8.3, 8.6_
  
  - [ ] 7.5 Update troubleshooting section in CICD_SETUP.md
    - Update troubleshooting for Jenkins-specific issues
    - Add Jenkins agent connectivity troubleshooting
    - Add Docker daemon access troubleshooting
    - Update authentication error troubleshooting for Jenkins credentials
    - Add stash/unstash artifact troubleshooting
    - Update MySQL container troubleshooting for Jenkins environment
    - Document how to view Jenkins console output and Blue Ocean UI
    - _Requirements: 14.6, 14.7, 1.6, 5.4, 4.6, 13.7_
  
  - [ ] 7.6 Update pipeline execution guide in CICD_SETUP.md
    - Document complete Jenkins pipeline flow from code push to Docker Hub
    - Explain CI stages: Checkout, Build, Start MySQL, Test, Coverage, Analysis, Quality Gate, Package
    - Explain CD stages: Docker Build, Docker Push (conditional on main branch)
    - Document how to view pipeline runs in Jenkins UI and Blue Ocean
    - Document how to manually trigger pipeline from Jenkins
    - Document how to access SonarQube dashboard and JaCoCo coverage reports
    - _Requirements: 14.5, 12.1, 12.2, 12.3, 12.4, 12.5, 12.6, 12.7_

- [ ] 8. Final checkpoint - Complete pipeline validation
  - Review all modified files: pom.xml, Jenkinsfile, lombok.config, CICD_SETUP.md
  - Verify XML structure is valid in pom.xml
  - Verify Groovy syntax is valid in Jenkinsfile
  - Verify .github/workflows directory is deleted or empty
  - Verify all requirements are addressed in implementation
  - Ensure all tests pass, ask the user if questions arise

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each task references specific requirements for traceability
- Checkpoints ensure incremental validation
- This is a DevOps/Infrastructure feature - no property-based tests are applicable
- The implementation focuses on configuration files (XML, Groovy) and documentation
- Jenkins credentials (sonar-token, docker-credentials) must be configured manually in Jenkins UI
- Jenkins Global Tool Configuration must include Maven-3.9 and JDK-21
- Jenkins agents must have Docker daemon access for MySQL container and Docker image operations
- The CD stages execute only on the main branch using conditional when directives
- Single Jenkinsfile replaces separate GitHub Actions workflow files
