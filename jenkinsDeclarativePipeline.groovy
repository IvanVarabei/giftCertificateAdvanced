#!groovy
properties([disableConcurrentBuilds(), pipelineTriggers([githubPush()])])

pipeline {
    agent any

    triggers {
        pollSCM('* * * * *')
    }

    stages {
        stage("Checkout") {
            steps {
                git branch: 'jenkins', changelog: true, poll: true, url:
                'https://github.com/IvanVarabei/giftCertificateAdvanced.git'
            }
        }
        stage("Build & Tests") {
            steps {
                script {
                    try {
                        sh 'gradle clean build codeCoverageReport'
                    } finally {
                        junit '**/build/test-results/test/*.xml'
                    }
                }
            }
        }
        stage("SonarQube analysis") {
            environment {
                scannerHome = tool 'InstalledSonar'
            }
            steps {
                withSonarQubeEnv('LocalSonar') {
                    bat "\"${scannerHome}\\bin\\sonar-scanner.bat\""
                    //-Dsonar.buildbreaker.skip=true"
                }
            }
        }
        // stage("Quality Gate") {
        //     steps {
        //         timeout(time: 20, unit: 'SECONDS') {
        //             waitForQualityGate abortPipeline: true
        //         }
        //     }
        // }
        stage("Deploy") {
            steps {
                deploy adapters: [tomcat9(credentialsId: 'tomcat',
                        path: '', url: 'http://localhost:8080/')],
                        contextPath: '/', onFailure: false, war: '**/*.war'
            }
        }
    }
    post {
        always {
            echo 'Build is completed'
        }
        success {
            echo 'Build is successful'
        }
        failure {
            echo 'Build is failed'
        }
        changed {
            echo 'The state of the Pipeline has changed'
        }
    }
}