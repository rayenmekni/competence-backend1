# Complete DevOps Setup Guide

This guide walks you through setting up the complete CI/CD pipeline with Kubernetes deployment for the Competence Backend application.

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Jenkins Setup](#jenkins-setup)
3. [Minikube Setup](#minikube-setup)
4. [Pipeline Configuration](#pipeline-configuration)
5. [Testing the Pipelines](#testing-the-pipelines)
6. [Accessing Services](#accessing-services)
7. [Troubleshooting](#troubleshooting)

---

## 🔧 Prerequisites

### Required Software

- **Java 21** - For building the Spring Boot application
- **Maven 3.9+** - For dependency management and builds
- **Docker Desktop** - For containerization (must be running)
- **Jenkins** - Running on http://localhost:7070
- **Minikube** - For local Kubernetes cluster
- **kubectl** - Kubernetes command-line tool
- **Git** - Version control

### Verify Installations

```powershell
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Docker
docker --version
docker ps

# Check Minikube
minikube version

# Check kubectl
kubectl version --client
```

---

## 🚀 Jenkins Setup

### 1. Install Required Plugins

Go to **Jenkins Dashboard → Manage Jenkins → Plugins → Available Plugins** and install:

- Maven Integration Plugin
- Docker Pipeline Plugin
- Kubernetes Plugin
- SonarQube Scanner Plugin
- JUnit Plugin
- JaCoCo Plugin

### 2. Configure Tools

Go to **Manage Jenkins → Tools**:

#### Maven Configuration
- Name: `Maven-3.9`
- Install automatically: ✅
- Version: 3.9.x

#### JDK Configuration
- Name: `JDK-21`
- Install automatically: ✅
- Version: 21

### 3. Configure Credentials

Go to **Manage Jenkins → Credentials → System → Global credentials**:

#### Docker Hub Credentials
- Kind: Username with password
- ID: `docker-credentials`
- Username: `rayenmekni123`
- Password: Your Docker Hub access token

#### SonarQube Token
- Kind: Secret text
- ID: `sonar-token`
- Secret: Your SonarQube token (generate after SonarQube is running)

### 4. Create Jenkins Jobs

#### CI Pipeline Job
1. Click **New Item**
2. Name: `competence-backend1-CI`
3. Type: **Pipeline**
4. Pipeline Definition: **Pipeline script from SCM**
5. SCM: **Git**
6. Repository URL: `https://github.com/rayenmekni/competence-backend1.git`
7. Branch: `*/main`
8. Script Path: `Jenkinsfile`

#### CD Pipeline Job
1. Click **New Item**
2. Name: `competence-backend1-CD`
3. Type: **Pipeline**
4. ✅ This project is parameterized:
   - String Parameter: `IMAGE_TAG` (default: `latest`)
   - String Parameter: `DOCKER_IMAGE` (default: `rayenmekni123/competence-backend1`)
5. Pipeline Definition: **Pipeline script from SCM**
6. SCM: **Git**
7. Repository URL: `https://github.com/rayenmekni/competence-backend1.git`
8. Branch: `*/main`
9. Script Path: `Jenkinsfile-CD`

---

## ☸️ Minikube Setup

### 1. Install Minikube

Download from: https://minikube.sigs.k8s.io/docs/start/

### 2. Start Minikube

```powershell
# Start Minikube with sufficient resources
minikube start --cpus=4 --memory=8192 --driver=docker

# Verify Minikube is running
minikube status

# Enable metrics server (for monitoring)
minikube addons enable metrics-server
```

### 3. Configure kubectl

```powershell
# Set kubectl context to Minikube
kubectl config use-context minikube

# Verify connection
kubectl cluster-info
kubectl get nodes
```

---

## ⚙️ Pipeline Configuration

### CI Pipeline (Jenkinsfile)

The CI pipeline automatically:
1. ✅ Checks out code from GitHub
2. ✅ Compiles Java source code
3. ✅ Starts MySQL container for tests
4. ✅ Runs unit tests
5. ✅ Generates JaCoCo coverage reports
6. ✅ Performs SonarQube analysis
7. ✅ Packages application as JAR
8. ✅ Builds Docker image
9. ✅ Pushes to Docker Hub
10. ✅ Triggers CD pipeline

### CD Pipeline (Jenkinsfile-CD)

The CD pipeline automatically:
1. ✅ Checks Minikube status
2. ✅ Creates/verifies namespace
3. ✅ Deploys MySQL
4. ✅ Deploys Backend application
5. ✅ Deploys SonarQube
6. ✅ Deploys Prometheus
7. ✅ Deploys Grafana
8. ✅ Verifies all deployments

---

## 🧪 Testing the Pipelines

### Method 1: Push to GitHub (Recommended)

```powershell
# Make a small change to trigger CI
echo "# CI/CD test" >> README.md
git add README.md
git commit -m "Test: Trigger CI/CD pipeline"
git push origin main
```

Then:
1. Go to Jenkins: http://localhost:7070
2. Watch `competence-backend1-CI` job execute
3. After CI completes, `competence-backend1-CD` will automatically start

### Method 2: Manual Trigger in Jenkins

#### Trigger CI Pipeline
1. Go to http://localhost:7070
2. Click on `competence-backend1-CI`
3. Click **Build Now**

#### Trigger CD Pipeline
1. Go to http://localhost:7070
2. Click on `competence-backend1-CD`
3. Click **Build with Parameters**
4. Set parameters:
   - `IMAGE_TAG`: `latest` (or specific commit SHA)
   - `DOCKER_IMAGE`: `rayenmekni123/competence-backend1`
5. Click **Build**

---

## 🌐 Accessing Services

After successful deployment, access your services:

### Backend API
- **URL**: http://localhost:30089
- **Health Check**: http://localhost:30089/actuator/health
- **Metrics**: http://localhost:30089/actuator/prometheus

### SonarQube
- **URL**: http://localhost:30900
- **Default Credentials**: admin/admin
- **First Login**: You'll be prompted to change the password

### Prometheus
- **URL**: http://localhost:30909
- **Targets**: http://localhost:30909/targets
- **Metrics**: Scrapes from Backend at `/actuator/prometheus`

### Grafana
- **URL**: http://localhost:30300
- **Default Credentials**: admin/admin
- **Data Source**: Prometheus (pre-configured)

### Verify Deployments

```powershell
# Check all resources in competence namespace
kubectl get all -n competence

# Check pod status
kubectl get pods -n competence

# Check services
kubectl get services -n competence

# Check deployments
kubectl get deployments -n competence
```

---

## 🔍 Troubleshooting

### Minikube Issues

```powershell
# Check Minikube status
minikube status

# Restart Minikube
minikube stop
minikube start

# Delete and recreate Minikube
minikube delete
minikube start --cpus=4 --memory=8192
```

### Pod Issues

```powershell
# Check pod logs
kubectl logs <pod-name> -n competence

# Describe pod for events
kubectl describe pod <pod-name> -n competence

# Get pod details
kubectl get pod <pod-name> -n competence -o yaml
```

### Service Issues

```powershell
# Check service endpoints
kubectl get endpoints -n competence

# Port forward to access service directly
kubectl port-forward service/backend-service 8089:8089 -n competence
```

### Docker Issues

```powershell
# Ensure Docker Desktop is running
docker ps

# Check Docker images
docker images | grep competence-backend1

# Pull image manually if needed
docker pull rayenmekni123/competence-backend1:latest
```

### Jenkins Issues

#### CI Pipeline Fails at MySQL Stage
- Ensure Docker Desktop is running
- Check if port 3307 is available
- Review MySQL container logs in Jenkins console

#### CD Pipeline Fails at Minikube Check
- Verify Minikube is running: `minikube status`
- Ensure kubectl is configured: `kubectl cluster-info`

#### SonarQube Analysis Fails
- Wait for SonarQube pod to be fully ready (takes 2-3 minutes)
- Generate token in SonarQube UI
- Update Jenkins credential `sonar-token`

### Common Commands

```powershell
# Restart a deployment
kubectl rollout restart deployment/<deployment-name> -n competence

# Delete a pod (will be recreated)
kubectl delete pod <pod-name> -n competence

# Scale deployment
kubectl scale deployment/<deployment-name> --replicas=3 -n competence

# View resource usage
kubectl top pods -n competence
kubectl top nodes
```

---

## 📊 Monitoring Setup

### Configure Grafana Dashboard

1. Access Grafana: http://localhost:30300
2. Login with admin/admin
3. Go to **Dashboards → Import**
4. Import Spring Boot dashboard (ID: 4701)
5. Select Prometheus data source
6. View metrics for your backend application

### Prometheus Targets

1. Access Prometheus: http://localhost:30909/targets
2. Verify `spring-boot-backend` target is UP
3. Query metrics: http://localhost:30909/graph

---

## 🎯 Next Steps

1. **Configure SonarQube Quality Gates**
   - Set up quality profiles
   - Configure quality gates
   - Add SonarQube badge to README

2. **Set Up Grafana Alerts**
   - Configure alert rules
   - Set up notification channels
   - Create alert dashboards

3. **Implement GitOps**
   - Use ArgoCD for continuous deployment
   - Automate Kubernetes manifest updates

4. **Add Integration Tests**
   - Create integration test suite
   - Add to CI pipeline

5. **Production Deployment**
   - Set up production Kubernetes cluster
   - Configure production secrets
   - Implement blue-green deployment

---

## 📚 Additional Resources

- [Jenkins Documentation](https://www.jenkins.io/doc/)
- [Minikube Documentation](https://minikube.sigs.k8s.io/docs/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [SonarQube Documentation](https://docs.sonarqube.org/)
- [Prometheus Documentation](https://prometheus.io/docs/)
- [Grafana Documentation](https://grafana.com/docs/)

---

## 🆘 Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review Jenkins console logs
3. Check Kubernetes pod logs
4. Verify all prerequisites are installed and running

---

**Last Updated**: May 5, 2026
**Version**: 1.0.0
