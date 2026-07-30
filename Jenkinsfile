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
                bat 'docker-compose down || exit 0'
                bat 'docker-compose up -d'
                powershell '''
                    $maxAttempts = 20
                    $attempt = 0
                    $ready = $false
                    while ($attempt -lt $maxAttempts -and -not $ready) {
                        try {
                            $response = Invoke-WebRequest -Uri http://localhost:4444/wd/hub/status -UseBasicParsing -TimeoutSec 2
                            if ($response.StatusCode -eq 200) { $ready = $true }
                        } catch {}
                        if (-not $ready) { Start-Sleep -Seconds 2; $attempt++ }
                    }
                    if (-not $ready) { throw "Selenium Grid did not become ready in time" }
                    Write-Host "Selenium Grid is ready."
                '''
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