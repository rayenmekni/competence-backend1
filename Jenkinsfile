pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9'
        jdk 'JDK-21'
    }
    
    environment {
        // Docker Hub credentials
        DOCKER_CREDS = credentials('docker-credentials')
        DOCKER_USERNAME = "${DOCKER_CREDS_USR}"
        DOCKER_PASSWORD = "${DOCKER_CREDS_PSW}"
        DOCKER_IMAGE = "rayenmekni123/competence-backend1"
        
        // SonarQube configuration
        SONAR_HOST_URL = 'http://localhost:9000'
        SONAR_TOKEN = credentials('sonar-token')
        
        // MySQL configuration for tests
        MYSQL_CONTAINER = "mysql-test-${BUILD_ID}"
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
                bat 'mvn clean compile -q'
            }
        }
        
        stage('Start MySQL') {
            steps {
                script {
                    echo "Starting MySQL 8.0 container..."
                    bat """
                        docker run -d ^
                            --name ${MYSQL_CONTAINER} ^
                            -e MYSQL_ROOT_PASSWORD=root ^
                            -e MYSQL_DATABASE=Competence ^
                            -p 3307:3306 ^
                            mysql:8.0
                    """
                    
                    echo "Waiting for MySQL to be ready..."
                    sleep(time: 30, unit: 'SECONDS')
                    
                    def pingResult = bat(
                        script: "docker exec ${MYSQL_CONTAINER} mysqladmin ping -h localhost --silent",
                        returnStatus: true
                    )
                    
                    if (pingResult != 0) {
                        echo "MySQL not ready after 30 seconds, waiting another 15 seconds..."
                        sleep(time: 15, unit: 'SECONDS')
                        
                        pingResult = bat(
                            script: "docker exec ${MYSQL_CONTAINER} mysqladmin ping -h localhost --silent",
                            returnStatus: true
                        )
                        
                        if (pingResult != 0) {
                            bat "docker logs ${MYSQL_CONTAINER}"
                            error "MySQL container failed to start within 45 seconds. Check logs above."
                        }
                    }
                    
                    echo "MySQL is ready for test execution"
                }
            }
        }
        
        stage('Tests Unitaires') {
            steps {
                echo "Running unit tests with MySQL integration..."
                bat 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('JaCoCo Coverage') {
            steps {
                echo "Generating JaCoCo coverage reports..."
                bat 'mvn jacoco:report'
                echo "Coverage reports generated in target/site/jacoco/"
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                script {
                    echo "Running SonarQube analysis..."
                    withSonarQubeEnv('SonarQube') {
                        bat """
                            mvn sonar:sonar ^
                                -Dsonar.projectKey=competence-backend1 ^
                                -Dsonar.host.url=%SONAR_HOST_URL% ^
                                -Dsonar.login=%SONAR_TOKEN% ^
                                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                        """
                    }
                }
            }
        }
        
        stage('Package JAR') {
            steps {
                echo "Packaging application as JAR..."
                bat 'mvn package -DskipTests'
                
                stash includes: 'target/*.jar', name: 'jar-artifact'
                echo "JAR artifact stashed for Docker build"
            }
        }
        
        stage('Docker Build') {
            steps {
                script {
                    echo "Building Docker image..."
                    
                    unstash 'jar-artifact'
                    echo "JAR artifact unstashed successfully"
                    
                    bat "dir target\\*.jar"
                    
                    bat """
                        docker build -t ${DOCKER_IMAGE}:latest ^
                                     -t ${DOCKER_IMAGE}:${env.GIT_COMMIT} ^
                                     .
                    """
                    echo "Docker image built: ${DOCKER_IMAGE}:latest"
                    echo "Docker image built: ${DOCKER_IMAGE}:${env.GIT_COMMIT}"
                }
            }
        }
        
        stage('Docker Push') {
            steps {
                script {
                    echo "Logging in to Docker Hub..."
                    def loginStatus = bat(
                        script: "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin",
                        returnStatus: true
                    )
                    
                    if (loginStatus != 0) {
                        error "Docker Hub authentication failed. Verify docker-credentials in Jenkins."
                    }
                    
                    echo "Pushing Docker images to Docker Hub..."
                    bat """
                        docker push ${DOCKER_IMAGE}:latest
                        docker push ${DOCKER_IMAGE}:${env.GIT_COMMIT}
                    """
                    
                    echo "Docker image pushed: ${DOCKER_IMAGE}:latest"
                    echo "Docker image pushed: ${DOCKER_IMAGE}:${env.GIT_COMMIT}"
                    echo "Docker Hub: https://hub.docker.com/r/rayenmekni123/competence-backend1"
                }
            }
        }
        
        stage('Trigger CD Pipeline') {
            steps {
                script {
                    echo "Triggering CD pipeline with IMAGE_TAG=${env.GIT_COMMIT}"
                    build job: 'competence-backend1-CD', 
                          parameters: [
                              string(name: 'IMAGE_TAG', value: "${env.GIT_COMMIT}"),
                              string(name: 'DOCKER_IMAGE', value: "${DOCKER_IMAGE}")
                          ], 
                          wait: false
                }
            }
        }
    }
    
    post {
        always {
            script {
                echo "Cleaning up MySQL container..."
                bat "docker stop ${MYSQL_CONTAINER} || exit 0"
                bat "docker rm ${MYSQL_CONTAINER} || exit 0"
            }
        }
        success {
            echo "✅ CI Pipeline completed successfully!"
        }
        failure {
            echo "❌ CI Pipeline failed. Check logs for details."
        }
    }
}
