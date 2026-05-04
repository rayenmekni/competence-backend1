pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9'
        jdk 'JDK-21'
    }
    
    environment {
        // Credentials from Jenkins Credentials store
        SONAR_TOKEN = credentials('sonar-token')
        DOCKER_CREDS = credentials('docker-credentials')
        DOCKER_USERNAME = "${DOCKER_CREDS_USR}"
        DOCKER_PASSWORD = "${DOCKER_CREDS_PSW}"
        
        // Configuration variables
        DOCKER_IMAGE = "${DOCKER_USERNAME}/competence-backend1"
        MYSQL_CONTAINER = "mysql-test-${BUILD_ID}"
        
        // Database connection for tests
        SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3307/Competence'
        SPRING_DATASOURCE_USERNAME = 'root'
        SPRING_DATASOURCE_PASSWORD = 'root'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo "Checked out branch: ${env.GIT_BRANCH}"
                echo "Commit SHA: ${env.GIT_COMMIT}"
            }
        }
        
        stage('Build') {
            steps {
                echo "Compiling Java source code..."
                sh 'mvn clean compile'
            }
        }
        
        stage('Start MySQL') {
            steps {
                script {
                    echo "Starting MySQL 8.0 container..."
                    sh """
                        docker run -d \
                            --name ${MYSQL_CONTAINER} \
                            -e MYSQL_ROOT_PASSWORD=root \
                            -e MYSQL_DATABASE=Competence \
                            -p 3307:3306 \
                            mysql:8.0
                    """
                    
                    echo "Waiting for MySQL to be ready..."
                    def ready = sh(
                        script: "timeout 60 bash -c 'until docker exec ${MYSQL_CONTAINER} mysqladmin ping -h localhost --silent; do sleep 2; done'",
                        returnStatus: true
                    )
                    
                    if (ready != 0) {
                        sh "docker logs ${MYSQL_CONTAINER}"
                        error "MySQL container failed to start within 60 seconds. Check logs above."
                    }
                    echo "MySQL is ready for test execution"
                }
            }
        }
        
        stage('Test') {
            steps {
                echo "Running unit tests with MySQL integration..."
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Coverage Report') {
            steps {
                echo "Generating JaCoCo coverage reports..."
                sh 'mvn jacoco:report'
                echo "Coverage reports generated in target/site/jacoco/"
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                script {
                    echo "⚠️ SonarQube analysis skipped - plugin not configured"
                    echo "To enable: Install SonarQube Scanner plugin and configure server"
                }
            }
        }
        
        stage('Package') {
            steps {
                echo "Packaging application as JAR..."
                sh 'mvn package -DskipTests'
                
                // Stash JAR for Docker build stage
                stash includes: 'target/*.jar', name: 'jar-artifact'
                echo "JAR artifact stashed for Docker build"
            }
        }
        
        stage('Docker Build') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo "Building Docker image..."
                    
                    // Unstash JAR artifact from Package stage
                    try {
                        unstash 'jar-artifact'
                        echo "JAR artifact unstashed successfully"
                    } catch (Exception e) {
                        error "JAR artifact not found. Package stage may have failed or artifact was not stashed. Check Package stage logs."
                    }
                    
                    // Verify JAR exists before Docker build
                    sh "ls -lh target/*.jar"
                    
                    // Build Docker image with multiple tags
                    sh """
                        docker build -t ${DOCKER_IMAGE}:latest \
                                     -t ${DOCKER_IMAGE}:${env.GIT_COMMIT} \
                                     .
                    """
                    echo "Docker image built: ${DOCKER_IMAGE}:latest"
                    echo "Docker image built: ${DOCKER_IMAGE}:${env.GIT_COMMIT}"
                }
            }
        }
        
        stage('Docker Push') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo "Logging in to Docker Hub..."
                    def loginStatus = sh(
                        script: "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin",
                        returnStatus: true
                    )
                    
                    if (loginStatus != 0) {
                        error "Docker Hub authentication failed. Verify docker-credentials in Jenkins (username: ${DOCKER_USERNAME}). Ensure Docker Hub access token is valid."
                    }
                    
                    echo "Pushing Docker images to Docker Hub..."
                    sh """
                        docker push ${DOCKER_IMAGE}:latest
                        docker push ${DOCKER_IMAGE}:${env.GIT_COMMIT}
                    """
                    
                    echo "Docker image pushed: ${DOCKER_IMAGE}:latest"
                    echo "Docker image pushed: ${DOCKER_IMAGE}:${env.GIT_COMMIT}"
                    echo "Docker Hub: https://hub.docker.com/r/${DOCKER_USERNAME}/competence-backend1"
                }
            }
        }
    }
    
    post {
        always {
            script {
                echo "Cleaning up MySQL container..."
                sh "docker stop ${MYSQL_CONTAINER} || true"
                sh "docker rm ${MYSQL_CONTAINER} || true"
            }
        }
        success {
            echo "✅ Pipeline completed successfully!"
        }
        failure {
            echo "❌ Pipeline failed. Check logs for details."
        }
    }
}
