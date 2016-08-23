#!groovy

def checkoutCode() {
    stage 'checkout'
     checkout scm: [$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/codesqueak/Z80Processor.git']]]
}

def build() {
    stage 'build'
    sh './gradlew clean build -x test'
}

def test() {
    stage 'test'
    sh './gradlew test'
}


def junitreport() {
    stage 'JUnit report'
    step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
}


stage 'execute Z80 build'

node {
  checkoutCode()
  build()
  test()
  junitreport()
}
