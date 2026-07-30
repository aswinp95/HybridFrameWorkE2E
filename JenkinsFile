pipeline {
    agent any

    tools {
        jdk 'Java'
        maven 'Maven'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/aswinp95/HybridFrameWorkE2E.git'
            }
        }

        stage('Start Selenium Grid') {
            steps {
                bat 'docker-compose up -d'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn test -DexecutionEnv=docker'
            }
        }

        stage('Stop Selenium Grid') {
            steps {
                bat 'docker-compose down'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'allure-results/**', allowEmptyArchive: true
        }
    }
}