# Jenkins CI/CD Pipeline Setup Guide

## 📋 Overview

This guide explains how to configure and use the complete Jenkins CI/CD pipeline for the **competence-backend1** microservice. The pipeline automatically builds, tests, analyzes code quality, creates Docker images, and publishes them to Docker Hub.

### Pipeline Flow

```
Code Push → Jenkins Pipeline → Docker Hub
    ↓              ↓                ↓
  Git SCM    CI Stages +       Published
             CD Stages          Image
```

**Jenkins Pipeline Stages:**
1. **Checkout** - Clone code from Git repository
2. **Build** - Compile Java source code with Maven
3. **Start MySQL** - Launch MySQL container for tests
4. **Test** - Run unit tests with MySQL integration
5. **Coverage Report** - Generate JaCoCo coverage reports
6. **SonarQube Analysis** - Static code analysis
7. **Quality Gate** - Evaluate SonarQube quality gate
8. **Package** - Build JAR artifact
9. **Docker Build** - Create Docker image (main branch only)
10. **Docker Push** - Publish to Docker Hub (main branch only)

---

## 🔧 Prerequisites

Before the pipeline can run successfully, you need to configure:

1. **Jenkins Server** - Running on localhost:7770 (as shown in your screenshot)
2. **Jenkins Credentials** - For SonarQube and Docker Hub
3. **Jenkins Global Tools** - Maven 3.9+ and JDK 21
4. **SonarQube Server** - Configured in Jenkins
5. **Docker** - Installed on Jenkins agent

---

## 🛠️ Jenkins Configuration

### Step 1: Configure Global Tools

1. Go to **Jenkins Dashboard** → **Manage Jenkins** → **Global Tool Configuration**

**Configure Maven:**
2. Scroll to **Maven** section
3. Click **"Add Maven"**
4. Name: `Maven-3.9`
5. Check **"Install automatically"**
6. Version: Select **Maven 3.9.0** or higher
7. Click **"Save"**

**Configure JDK:**
8. Scroll to **JDK** section
9. Click **"Add JDK"**
10. Name: `JDK-21`
11. Check **"Install automatically"**
12. Select **"Install from adoptium.net"**
13. Version: Select **jdk-21+35** or latest Java 21
14. Click **"Save"**

---

### Step 2: Configure SonarQube Server

1. Go to **Jenkins Dashboard** → **Manage Jenkins** → **Configure System**
2. Scroll to **SonarQube servers** section
3. Click **"Add SonarQube"**
4. Name: `SonarQube` (must match Jenkinsfile)
5. Server URL: `https://sonarcloud.io` (or your self-hosted SonarQube URL)
6. Server authentication token: Select credential (create in next step)
7. Click **"Save"**

---

### Step 3: Configure Jenkins Credentials

#### A. SonarQube Token (sonar-token)

1. Go to **Jenkins Dashboard** → **Manage Jenkins** → **Manage Credentials**
2. Click on **(global)** domain
3. Click **"Add Credentials"**
4. **Kind**: Secret text
5. **Scope**: Global
6. **Secret**: Paste your SonarQube token (generate from SonarCloud - see below)
7. **ID**: `sonar-token`
8. **Description**: SonarQube authentication token
9. Click **"Create"**

#### B. Docker Hub Credentials (docker-credentials)

1. Click **"Add Credentials"** again
2. **Kind**: Username with password
3. **Scope**: Global
4. **Username**: Your Docker Hub username (e.g., `rayenmekni`)
5. **Password**: Your Docker Hub access token (generate from Docker Hub - see below)
6. **ID**: `docker-credentials`
7. **Description**: Docker Hub authentication
8. Click **"Create"**

---

## 🔍 SonarQube Setup

### Step 1: Create SonarCloud Account

1. Go to [https://sonarcloud.io](https://sonarcloud.io)
2. Click **"Log in"** and choose **"With GitHub"**
3. Authorize SonarCloud to access your GitHub account

### Step 2: Create SonarCloud Project

1. Once logged in, click **"+"** (top right) → **"Analyze new project"**
2. Select your repository: **`rayenmekni/competence-backend1`**
3. Click **"Set Up"**
4. Choose **"Other CI"** (not GitHub Actions)

### Step 3: Generate SONAR_TOKEN

1. In SonarCloud, go to **My Account** (top right) → **Security**
2. Under **"Generate Tokens"**, enter a name: `competence-backend1-jenkins`
3. Type: **Global Analysis Token**
4. Expires in: **90 days** (or No expiration)
5. Click **"Generate"**
6. **Copy the token immediately** (you won't be able to see it again)

### Step 4: Configure SonarQube Webhook (for Quality Gate)

1. In SonarCloud, go to your project → **Administration** → **Webhooks**
2. Click **"Create"**
3. Name: `Jenkins`
4. URL: `http://localhost:7770/sonarqube-webhook/` (replace with your Jenkins URL)
5. Secret: Leave empty
6. Click **"Create"**

**Note:** If Jenkins is not publicly accessible, Quality Gate stage will timeout but pipeline will continue.

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
5. Description: `competence-backend1-jenkins`
6. Access permissions: **Read, Write, Delete**
7. Click **"Generate"**
8. **Copy the token immediately** (you won't be able to see it again)

---

## 🚀 Creating Jenkins Job

### Step 1: Create Pipeline Job

1. Go to **Jenkins Dashboard** → **New Item**
2. Enter name: `competence-backend1-pipeline`
3. Select **"Pipeline"**
4. Click **"OK"**

### Step 2: Configure Pipeline

1. **General** section:
   - Description: `CI/CD pipeline for competence-backend1 microservice`

2. **Build Triggers** section:
   - Check **"Poll SCM"** for automatic builds
   - Schedule: `H/5 * * * *` (poll every 5 minutes)
   - OR configure GitHub webhook for instant triggers

3. **Pipeline** section:
   - Definition: **Pipeline script from SCM**
   - SCM: **Git**
   - Repository URL: `https://github.com/rayenmekni/competence-backend1.git`
   - Credentials: Add your GitHub credentials if private repo
   - Branch Specifier: `*/main`
   - Script Path: `Jenkinsfile`

4. Click **"Save"**

---

## 🎯 Running the Pipeline

### Manual Execution

1. Go to your pipeline job: `competence-backend1-pipeline`
2. Click **"Build Now"**
3. Watch the pipeline execute in real-time

### Automatic Execution

The pipeline runs automatically when:
- Code is pushed to the `main` branch (if webhook configured)
- SCM polling detects changes (every 5 minutes)

### View Pipeline Execution

**Classic View:**
1. Click on a build number (e.g., #1, #2)
2. Click **"Console Output"** to see logs

**Blue Ocean View (Recommended):**
1. Install Blue Ocean plugin: **Manage Jenkins** → **Manage Plugins** → Search "Blue Ocean"
2. Click **"Open Blue Ocean"** in sidebar
3. View visual pipeline with stage-by-stage execution

---

## 📊 Viewing Reports

### JaCoCo Coverage Report

**Option 1: In Jenkins**
1. Go to your pipeline build
2. Click **"Console Output"**
3. Search for "Coverage reports generated in target/site/jacoco/"

**Option 2: Locally**
1. Run tests locally: `./mvnw clean test`
2. Open: `target/site/jacoco/index.html` in your browser

### SonarQube Analysis

1. Go to [https://sonarcloud.io](https://sonarcloud.io)
2. Click on your project: **competence-backend1**
3. View:
   - **Quality Gate** status (Passed/Failed)
   - **Code Coverage** percentage
   - **Bugs**, **Vulnerabilities**, **Code Smells**
   - **Security Hotspots**
   - **Maintainability Rating**

**Direct Link:** The Jenkins console output includes the SonarQube dashboard URL.

### Docker Hub Image

1. Go to [https://hub.docker.com](https://hub.docker.com)
2. Navigate to your repository: `<your-username>/competence-backend1`
3. View:
   - **Tags**: `latest` and commit SHA tags
   - **Image size**
   - **Last pushed** timestamp

---

## 🐛 Troubleshooting

### ❌ Pipeline Fails: Maven or JDK Not Found

**Error Message:**
```
ERROR: Maven-3.9 not found
```

**Solution:**
1. Go to **Manage Jenkins** → **Global Tool Configuration**
2. Verify Maven installation named `Maven-3.9` exists
3. Verify JDK installation named `JDK-21` exists
4. Ensure "Install automatically" is checked
5. Save and re-run pipeline

---

### ❌ Pipeline Fails: SonarQube Authentication Error

**Error Message:**
```
Error: Not authorized. Please check the user token
```

**Solution:**
1. Go to **Manage Jenkins** → **Manage Credentials**
2. Verify credential with ID `sonar-token` exists
3. Regenerate token in SonarCloud if expired:
   - SonarCloud → My Account → Security → Generate Token
4. Update Jenkins credential with new token
5. Re-run pipeline

---

### ❌ Pipeline Fails: Docker Hub Authentication Error

**Error Message:**
```
Error: unauthorized: authentication required
```

**Solution:**
1. Go to **Manage Jenkins** → **Manage Credentials**
2. Verify credential with ID `docker-credentials` exists
3. Ensure password is an **access token**, not your account password
4. Regenerate access token in Docker Hub if expired:
   - Docker Hub → Account Settings → Security → New Access Token
5. Update Jenkins credential with new token
6. Re-run pipeline

---

### ❌ Pipeline Fails: MySQL Container Error

**Error Message:**
```
Error: MySQL container failed to start
```

**Solution:**
1. Verify Docker is installed on Jenkins agent:
   ```bash
   docker --version
   ```
2. Verify Jenkins user has Docker permissions:
   ```bash
   sudo usermod -aG docker jenkins
   sudo systemctl restart jenkins
   ```
3. Check if port 3307 is already in use:
   ```bash
   netstat -tuln | grep 3307
   ```
4. Re-run pipeline

---

### ❌ Pipeline Fails: Docker Build Error

**Error Message:**
```
Error: Cannot connect to Docker daemon
```

**Solution:**
1. Verify Docker daemon is running:
   ```bash
   sudo systemctl status docker
   ```
2. Start Docker if stopped:
   ```bash
   sudo systemctl start docker
   ```
3. Verify Jenkins user has Docker access:
   ```bash
   sudo usermod -aG docker jenkins
   sudo systemctl restart jenkins
   ```
4. Re-run pipeline

---

### ❌ CD Stages Don't Execute

**Observation:**
Docker Build and Docker Push stages are skipped

**Solution:**
1. CD stages only run on `main` branch
2. Verify you're building the `main` branch:
   - Check Jenkins console output for "Checked out branch: origin/main"
3. If on a different branch, merge to `main` or push directly to `main`

---

### ❌ Quality Gate Stage Timeout

**Error Message:**
```
Timeout waiting for quality gate
```

**Solution:**
1. This is expected if SonarQube webhook is not configured
2. Configure webhook in SonarCloud:
   - Project → Administration → Webhooks
   - URL: `http://your-jenkins-url/sonarqube-webhook/`
3. If Jenkins is not publicly accessible, the pipeline will continue anyway (by design)
4. Check SonarQube dashboard manually for quality gate status

---

### ❌ Tests Fail: Database Connection Error

**Error Message:**
```
Communications link failure
```

**Solution:**
1. Verify MySQL container started successfully:
   - Check Jenkins console output for "MySQL is ready for test execution"
2. Increase MySQL wait timeout in Jenkinsfile if needed:
   ```groovy
   timeout 90 bash -c '...'  // Increase from 60 to 90 seconds
   ```
3. Verify environment variables are set correctly:
   - `SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3307/Competence`
4. Re-run pipeline

---

## 📚 Jenkins Agent Requirements

For the pipeline to work, the Jenkins agent must have:

✅ **Docker** - Version 20.10+ with daemon running
✅ **Maven** - Configured in Global Tool Configuration as `Maven-3.9`
✅ **JDK 21** - Configured in Global Tool Configuration as `JDK-21`
✅ **Network Access** - To SonarCloud and Docker Hub
✅ **Permissions** - Jenkins user in `docker` group

**Verify Agent Setup:**
```bash
# Check Docker
docker --version
docker ps

# Check Jenkins user Docker access
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins

# Check network access
curl -I https://sonarcloud.io
curl -I https://hub.docker.com
```

---

## 🎓 For Your Professor

This Jenkins pipeline demonstrates:

✅ **CI Pipeline Components:**
- Automated unit testing with MySQL Docker container
- Code coverage measurement with JaCoCo
- Static code analysis with SonarQube
- Artifact creation (JAR file) with stash/unstash

✅ **CD Pipeline Components:**
- Automated Docker image creation
- Docker Hub publication
- Image tagging (latest + commit SHA)
- Conditional execution (main branch only)

✅ **Pipeline Automation:**
- Single Jenkinsfile with declarative syntax
- Sequential stage execution
- CD stages execute only after successful CI
- Automatic cleanup with post-build actions

✅ **Quality Metrics:**
- Coverage reports visible in SonarQube
- Quality gate evaluation
- Code smell detection
- Security vulnerability scanning

✅ **Jenkins Best Practices:**
- Credentials stored securely in Jenkins Credentials store
- Tools configured in Global Tool Configuration
- Proper error handling and logging
- MySQL container cleanup in post-always block

---

## 📞 Support

If you encounter issues not covered in this guide:

1. Check **Jenkins Console Output** for detailed error logs
2. Review the **Troubleshooting** section above
3. Verify all credentials are configured correctly in Jenkins
4. Verify Global Tool Configuration has Maven-3.9 and JDK-21
5. Ensure Docker daemon is running on Jenkins agent
6. Check SonarQube server configuration in Jenkins

---

## 🔗 Additional Resources

- **Jenkins Documentation**: [https://www.jenkins.io/doc/](https://www.jenkins.io/doc/)
- **Jenkins Pipeline Syntax**: [https://www.jenkins.io/doc/book/pipeline/syntax/](https://www.jenkins.io/doc/book/pipeline/syntax/)
- **SonarQube Documentation**: [https://docs.sonarcloud.io](https://docs.sonarcloud.io)
- **Docker Hub Documentation**: [https://docs.docker.com/docker-hub/](https://docs.docker.com/docker-hub/)
- **JaCoCo Documentation**: [https://www.jacoco.org/jacoco/trunk/doc/](https://www.jacoco.org/jacoco/trunk/doc/)

---

**Last Updated**: 2026-05-03
**Pipeline Version**: 2.0 (Jenkins)
**Jenkins URL**: http://localhost:7770
**Maintainer**: Rayen Mekni
