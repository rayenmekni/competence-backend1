# ✅ Jenkins Migration Complete

## What Was Done

Successfully migrated from GitHub Actions to Jenkins pipeline.

### Files Created:
1. **Jenkinsfile** - Complete Jenkins declarative pipeline with 10 stages
2. **CICD_SETUP.md** - Updated documentation for Jenkins setup

### Files Deleted:
1. **.github/workflows/ci.yml** - Replaced by Jenkinsfile
2. **.github/workflows/cd.yml** - Replaced by Jenkinsfile

### Files Kept (No Changes Needed):
1. **pom.xml** - Already configured with JaCoCo and SonarQube
2. **Dockerfile** - Already correct
3. **lombok.config** - Already configured

---

## 🚀 Next Steps

### 1. Configure Jenkins (REQUIRED)

Before running the pipeline, configure Jenkins:

#### A. Global Tool Configuration
Go to: **Manage Jenkins** → **Global Tool Configuration**

- **Maven**: Name = `Maven-3.9`, Version = 3.9.0+
- **JDK**: Name = `JDK-21`, Version = Java 21

#### B. Jenkins Credentials
Go to: **Manage Jenkins** → **Manage Credentials** → **(global)**

- **sonar-token**: Secret text with your SonarQube token
- **docker-credentials**: Username with password (Docker Hub)

#### C. SonarQube Server
Go to: **Manage Jenkins** → **Configure System** → **SonarQube servers**

- **Name**: `SonarQube`
- **Server URL**: `https://sonarcloud.io`
- **Token**: Select `sonar-token` credential

---

### 2. Create Jenkins Job

1. **Jenkins Dashboard** → **New Item**
2. Name: `competence-backend1-pipeline`
3. Type: **Pipeline**
4. **Pipeline** section:
   - Definition: **Pipeline script from SCM**
   - SCM: **Git**
   - Repository URL: `https://github.com/rayenmekni/competence-backend1.git`
   - Branch: `*/main`
   - Script Path: `Jenkinsfile`
5. Click **Save**

---

### 3. Run the Pipeline

1. Go to your pipeline job
2. Click **"Build Now"**
3. Watch it execute!

---

## 📋 Pipeline Stages

The Jenkinsfile includes these stages:

1. ✅ **Checkout** - Clone code from Git
2. ✅ **Build** - Compile with Maven
3. ✅ **Start MySQL** - Launch MySQL container
4. ✅ **Test** - Run unit tests
5. ✅ **Coverage Report** - Generate JaCoCo reports
6. ✅ **SonarQube Analysis** - Static code analysis
7. ✅ **Quality Gate** - Evaluate quality gate
8. ✅ **Package** - Build JAR artifact
9. ✅ **Docker Build** - Create Docker image (main branch only)
10. ✅ **Docker Push** - Publish to Docker Hub (main branch only)

---

## 🔑 Required Credentials

### SonarQube Token
- Generate from: https://sonarcloud.io → My Account → Security
- Add to Jenkins as: `sonar-token` (Secret text)

### Docker Hub Token
- Generate from: https://hub.docker.com → Account Settings → Security
- Add to Jenkins as: `docker-credentials` (Username with password)

---

## 📖 Documentation

Read **CICD_SETUP.md** for:
- Complete Jenkins configuration guide
- SonarQube setup instructions
- Docker Hub setup instructions
- Troubleshooting common issues
- Jenkins agent requirements

---

## ✨ Key Differences from GitHub Actions

| GitHub Actions | Jenkins |
|----------------|---------|
| `.github/workflows/*.yml` | `Jenkinsfile` |
| GitHub Secrets | Jenkins Credentials |
| `upload-artifact` / `download-artifact` | `stash` / `unstash` |
| Service containers | Manual Docker containers |
| `workflow_run` trigger | Sequential stages with `when` |
| Automatic runners | Configure agents/tools |

---

## 🎯 Ready to Test!

Your Jenkins pipeline is ready. Just:
1. Configure Jenkins (tools, credentials, SonarQube server)
2. Create the pipeline job
3. Click "Build Now"

Good luck! 🚀
