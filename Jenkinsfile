#!groovy

pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps { //Checking out the repo
                checkout scm: [$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/codesqueak/Z80Processor.git']]]
                echo env.BRANCH_NAME
                sh 'echo $BRANCH_NAME'

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
                echo env.BRANCH_NAME
                sh 'echo $BRANCH_NAME'
            }
        }

    }

}