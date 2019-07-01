#!groovy

pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps { //Checking out the repo
                checkout scm: [$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/codesqueak/Z80Processor.git']]]
                echo env.GIT_BRANCH
                sh 'echo $GIT_BRANCH'
            }
        }

        stage('env') {
            steps { //Check env variables
                echo sh(script: 'env|sort', returnStdout: true)
            }
        }

        stage('Build') {
            steps { //Build using jenkins
                sh './gradlew clean build -x test'
            }
        }

        stage('Jar') {
            steps { //Make a jar file
                sh './gradlew jar'
            }
        }

        stage('branch') {
            steps { //branch check
                echo env.GIT_BRANCH
                sh 'echo $GIT_BRANCH'
            }
        }

        stage ('master') {
            when {
                expression { env.GIT_BRANCH == 'origin/master' }
            }
            steps {
                echo "Hello origin/master"
            }
        }

    }

}