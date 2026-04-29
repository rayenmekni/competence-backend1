# CI/CD Pipeline Setup Guide

## 📋 Overview

This guide explains how to configure and use the complete CI/CD pipeline for the **competence-backend1** microservice. The pipeline automatically builds, tests, analyzes code quality, creates Docker images, and publishes them to Docker Hub.

### Pipeline Flow

```
Code Push → CI Pipeline → CD Pipeline → Docker Hub
    ↓           ↓              ↓            ↓
  GitHub    Tests +        Build +      Published
            Coverage       Docker       Image
            + Sonar        Image
```

**CI Pipeline (Continuous Integration):**
1. Checkout code
2. Set up Java 21
3. Start MySQL service
4. Run unit tests
5. Generate JaCoCo coverage report
6. Run SonarCloud analysis
7. Build JAR artifact
8. Upload JAR to GitHub Actions

**CD Pipeline (Continuous Deployment):**
1. Triggered automatically when CI succeeds
2. Download JAR artifact from CI
3. Login to Docker Hub
4. Build Docker image
5. Push image to Docker Hub with tags: `latest` and commit SHA

---

## 🔧 Prerequisites

Before the pipeline can run successfully, you need to configure three secrets in your GitHub repository:

1. **SONAR_TOKEN** - For SonarCloud code analysis
2. **DOCKER_USERNAME** - Your Docker Hub username
3. **DOCKER_PASSWORD** - Your Docker Hub access token

---

## 🔍 SonarCloud Setup

### Step 1: Create SonarCloud Account

1. Go to [https://sonarcloud.io](https://sonarcloud.io)
2. Click **"Log in"** and choose **"With GitHub"**
3. Authorize SonarCloud to access your GitHub account

### Step 2: Create SonarCloud Project

1. Once logged in, click **"+"** (top right) → **"Analyze new project"**
2. Select your repository: **`rayenmekni/competence-backend1`**
3. Click **"Set Up"**
4. Choose **"With GitHub Actions"** as the analysis method

### Step 3: Generate SONAR_TOKEN

1. In SonarCloud, go to **My Account** (top right) → **Security**
2. Under **"Generate Tokens"**, enter a name: `competence-backend1-ci`
3. Click **"Generate"**
4. **Copy the token immediately** (you won't be able to see it again)

### Step 4: Add SONAR_TOKEN to GitHub Secrets

1. Go to your GitHub repository: [https://github.com/rayenmekni/competence-backend1](https://github.com/rayenmekni/competence-backend1)
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **"New repository secret"**
4. Name: `SONAR_TOKEN`
5. Value: Paste the token you copied from SonarCloud
6. Click **"Add secret"**

### Step 5: Configure SonarCloud Project

The project is already configured in `pom.xml` with:
- **Project Key**: `rayenmekni_competence-backend1`
- **Organization**: `rayenmekni`

If your SonarCloud organization name is different, update the `sonar.organization` property in `pom.xml`.

---

## 🐳 Docker Hub Setup

### Step 1: Create Docker Hub Account

1. Go to [https://hub.docker.com](https://hub.docker.com)
2. Click **"Sign Up"** and create an account
3. Verify your email address

### Step 2: Generate Access Token

1. Log in to Docker Hub
2. Click your username (top right) → **Account Settings**
3. Go to **Security** → **Access Tokens**
4. Click **"New Access Token"**
5. Description: `competence-backend1-ci`
6. Access permissions: **Read, Write, Delete**
7. Click **"Generate"**
8. **Copy the token immediately** (you won't be able to see it again)

### Step 3: Add Docker Hub Secrets to GitHub

1. Go to your GitHub repository: [https://github.com/rayenmekni/competence-backend1](https://github.com/rayenmekni/competence-backend1)
2. Click **Settings** → **Secrets and variables** → **Actions**

**Add DOCKER_USERNAME:**
3. Click **"New repository secret"**
4. Name: `DOCKER_USERNAME`
5. Value: Your Docker Hub username (e.g., `rayenmekni`)
6. Click **"Add secret"**

**Add DOCKER_PASSWORD:**
7. Click **"New repository secret"** again
8. Name: `DOCKER_PASSWORD`
9. Value: Paste the access token you copied from Docker Hub
10. Click **"Add secret"**

---

## ✅ Verify Secrets Configuration

After adding all secrets, verify they are configured correctly:

1. Go to **Settings** → **Secrets and variables** → **Actions**
2. You should see three secrets:
   - ✅ `SONAR_TOKEN`
   - ✅ `DOCKER_USERNAME`
   - ✅ `DOCKER_PASSWORD`

---

## 🚀 Running the Pipeline

### Automatic Execution

The pipeline runs automatically on every push to the `main` branch:

```bash
git add .
git commit -m "Your commit message"
git push origin main
```

### View Pipeline Execution

1. Go to your GitHub repository
2. Click the **"Actions"** tab
3. You'll see two workflows:
   - **CI Backend** - Runs first (tests, coverage, analysis)
   - **CD Backend** - Runs automatically after CI succeeds

### CI Pipeline Steps

Click on a **"CI Backend"** run to see:
- ✅ Checkout code
- ✅ Set up Java 21
- ✅ Fix mvnw permissions
- ✅ Wait for MySQL
- ✅ Run tests
- ✅ Generate JaCoCo coverage report
- ✅ SonarCloud analysis
- ✅ Build JAR artifact
- ✅ Upload JAR artifact

### CD Pipeline Steps

Click on a **"CD Backend"** run to see:
- ✅ Checkout code
- ✅ Download JAR artifact
- ✅ Login to Docker Hub
- ✅ Set up Docker Buildx
- ✅ Build and push Docker image
- ✅ Log Docker image details

---

## 📊 Viewing Reports

### JaCoCo Coverage Report

**Option 1: In GitHub Actions**
1. Go to **Actions** → Select a CI run
2. Scroll to **"Generate JaCoCo coverage report"** step
3. View coverage summary in logs

**Option 2: Locally**
1. Run tests locally: `./mvnw clean test`
2. Open: `target/site/jacoco/index.html` in your browser

### SonarCloud Analysis

1. Go to [https://sonarcloud.io](https://sonarcloud.io)
2. Click on your project: **competence-backend1**
3. View:
   - **Quality Gate** status (Passed/Failed)
   - **Code Coverage** percentage
   - **Bugs**, **Vulnerabilities**, **Code Smells**
   - **Security Hotspots**
   - **Maintainability Rating**

**Direct Link:** The CI pipeline logs include a direct link to the SonarCloud dashboard.

### Docker Hub Image

1. Go to [https://hub.docker.com](https://hub.docker.com)
2. Navigate to your repository: `<your-username>/competence-backend1`
3. View:
   - **Tags**: `latest` and commit SHA tags
   - **Image size**
   - **Last pushed** timestamp

---

## 🐛 Troubleshooting

### ❌ CI Pipeline Fails: SonarCloud Authentication Error

**Error Message:**
```
Error: SONAR_TOKEN is missing or invalid
```

**Solution:**
1. Verify `SONAR_TOKEN` is added to GitHub Secrets
2. Regenerate token in SonarCloud if expired
3. Update the secret in GitHub with the new token

---

### ❌ CD Pipeline Fails: Docker Hub Authentication Error

**Error Message:**
```
Error: unauthorized: authentication required
```

**Solution:**
1. Verify `DOCKER_USERNAME` and `DOCKER_PASSWORD` are added to GitHub Secrets
2. Ensure `DOCKER_PASSWORD` is an **access token**, not your account password
3. Regenerate access token in Docker Hub if expired
4. Update the secret in GitHub with the new token

---

### ❌ CD Pipeline Fails: Missing Artifact

**Error Message:**
```
Error: Unable to find artifact application-jar
```

**Solution:**
1. Verify CI pipeline completed successfully
2. Check that CI pipeline uploaded the artifact (look for "Upload JAR artifact" step)
3. Ensure CD pipeline is triggered by the correct CI workflow name: "CI Backend"

---

### ❌ CI Pipeline Fails: MySQL Connection Error

**Error Message:**
```
Error: Communications link failure
```

**Solution:**
1. The MySQL service may not be ready yet
2. Increase the wait time in `ci.yml`:
   ```yaml
   - name: Wait for MySQL
     run: sleep 25  # Increase from 15 to 25 seconds
   ```

---

### ❌ Tests Fail Locally But Pass in CI

**Solution:**
1. Ensure MySQL is running locally:
   ```bash
   docker run -d -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=Competence mysql:8.0
   ```
2. Set environment variables:
   ```bash
   export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/Competence
   export SPRING_DATASOURCE_USERNAME=root
   export SPRING_DATASOURCE_PASSWORD=root
   ```
3. Run tests:
   ```bash
   ./mvnw clean test
   ```

---

### ❌ SonarCloud Shows "No Coverage Data"

**Solution:**
1. Verify JaCoCo report is generated:
   ```bash
   ./mvnw clean test
   ls -la target/site/jacoco/jacoco.xml
   ```
2. Ensure `sonar.coverage.jacoco.xmlReportPaths` in `pom.xml` points to the correct path
3. Re-run the CI pipeline

---

## 📚 Additional Resources

- **SonarCloud Documentation**: [https://docs.sonarcloud.io](https://docs.sonarcloud.io)
- **Docker Hub Documentation**: [https://docs.docker.com/docker-hub/](https://docs.docker.com/docker-hub/)
- **GitHub Actions Documentation**: [https://docs.github.com/en/actions](https://docs.github.com/en/actions)
- **JaCoCo Documentation**: [https://www.jacoco.org/jacoco/trunk/doc/](https://www.jacoco.org/jacoco/trunk/doc/)

---

## 🎓 For Your Professor

This pipeline demonstrates:

✅ **CI Pipeline Components:**
- Automated unit testing with MySQL integration
- Code coverage measurement with JaCoCo
- Static code analysis with SonarCloud
- Artifact creation (JAR file)

✅ **CD Pipeline Components:**
- Automated Docker image creation
- Docker Hub publication
- Image tagging (latest + commit SHA)

✅ **Pipeline Automation:**
- CI automatically triggers on code push
- CD automatically triggers when CI succeeds
- No manual intervention required

✅ **Quality Metrics:**
- Coverage reports visible in SonarCloud
- Quality gate enforcement
- Code smell detection
- Security vulnerability scanning

---

## 📞 Support

If you encounter issues not covered in this guide:

1. Check the **Actions** tab in GitHub for detailed error logs
2. Review the **Troubleshooting** section above
3. Verify all secrets are configured correctly
4. Ensure your SonarCloud organization name matches the one in `pom.xml`

---

**Last Updated**: 2026-04-29
**Pipeline Version**: 1.0
**Maintainer**: Rayen Mekni
